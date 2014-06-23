package ar.com.glasit.rom.Activities;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;

import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.Coupon;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

public class CameraActivity extends Activity {

    private Coupon coupon;
    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    ImageScanner scanner;
    ProgressBar progressBar;
    FrameLayout preview;

    static {
        System.loadLibrary("iconv");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        scanText = (TextView)findViewById(R.id.scanText);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
        finish();
    }

    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (mCamera != null)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                preview.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    try {
                        coupon = new Coupon(sym.getData());
                    } catch (Exception e) {

                    }
                    break;
                }
                validateCoupon();
            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private void validateCoupon() {
        if (coupon == null){
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCB);
            preview.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

//        JSONObject json = new JSONObject();
//        try {
        	
        	   List<NameValuePair> params = new Vector<NameValuePair>();
               params.add(new BasicNameValuePair("idRestaurant", BackendHelper.getSecretKey()));
               params.add(new BasicNameValuePair("idPromocion", Long.toString(coupon.getId())));
               RestService.callGetService(onCouponValidation, WellKnownMethods.ValidateCoupon, params);
//        	
//            if (coupon.getExpirationDate().after(new Date()) &&
//                    coupon.getStartDate().before(new Date())){
//                json.put("success", true);
//            } else {
//                json.put("success", false);
//            }
//            onCouponValidation.onServiceCompleted(null, new ServiceResponse(json));
//        } catch (JSONException e) {
//            onCouponValidation.onError("");
//        }
    }

    private String buildMessage(int resId, Coupon coupon){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("d-MMM-yyyy");
        String msg = getString(resId);
        msg = msg.replace("{Coupon.title}", coupon.getTitle());
        msg = msg.replace("{Coupon.id}", Long.toString(coupon.getId()));
        return msg;
    }

    private class OnCouponAccepted implements DialogInterface.OnClickListener {

        Coupon coupon;

        OnCouponAccepted(Coupon coupon) {
            this.coupon = coupon;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            releaseCamera();
            CameraActivity.this.getIntent().putExtra("COUPON", coupon.toString());
            CameraActivity.this.setResult(RESULT_OK, getIntent());
            CameraActivity.this.finish();
        }
    }

    private ServiceListener onCouponValidation = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse obj) {
            try {
                AlertDialog.Builder alert = new AlertDialog.Builder(CameraActivity.this);
                if (obj.getSuccess()) {
                    alert.setMessage(buildMessage(R.string.cuponConfirmation, coupon));
                    alert.setPositiveButton("Ok", new OnCouponAccepted(coupon));
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCamera.setPreviewCallback(previewCb);
                            mCamera.startPreview();
                            mCamera.autoFocus(autoFocusCB);
                            preview.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    alert.setMessage(R.string.invalidQR);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCamera.setPreviewCallback(previewCb);
                            mCamera.startPreview();
                            mCamera.autoFocus(autoFocusCB);
                            preview.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                alert.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {
            Toast.makeText(CameraActivity.this, R.string.connectivity_error, Toast.LENGTH_SHORT).show();
            mCamera.setPreviewCallback(previewCb);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCB);
            preview.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };
}