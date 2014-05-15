package ar.com.glasit.rom.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;
import ar.com.glasit.rom.Model.Addition;
import ar.com.glasit.rom.Model.ItemProduct;
import ar.com.glasit.rom.Model.ItemSubRubro;
import ar.com.glasit.rom.Model.Order;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.WellKnownMethods;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.view.MenuItem;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Vector;

public class RejectOrderDialog extends SherlockDialogFragment{

    private Order order;
    private OnRejectListener onRejectListener;
    private OnCancelListener onCancelListener;
    private OnRejectAndReorderListener onRejectAndReorderListener;

    private EditText input;

    public RejectOrderDialog(Order order) {
        this.order = order;
    }

    public void setOnRejectListener(OnRejectListener listener) {
        this.onRejectListener = listener;
    }

    public void setOnCancelListener(OnCancelListener listener) {
        this.onCancelListener = listener;
    }

    public void setOnRejectAndReorderListener(OnRejectAndReorderListener listener) {
        this.onRejectAndReorderListener = listener;
    }

    public interface OnCancelListener {
        void onSubmitListener(Order order);
    }

    public interface OnRejectListener {
        void onReject(Order order, String comment);
    }

    public interface OnRejectAndReorderListener {
        void onRejectAndReorder(Order order, String comment);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(getSherlockActivity());
        dialog.getWindow().setTitle(order.toString());
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_order);
        dialog.show();

        dialog.setTitle(order.toString());
        input = (EditText) dialog.findViewById(R.id.input);

        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelListener != null)
                    onCancelListener.onSubmitListener(order);
                dismiss();
            }
        });
        Button reject = (Button) dialog.findViewById(R.id.reject);
        reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRejectListener != null)
                    onRejectListener.onReject(order, input.getText().toString());
                dismiss();
            }
        });
        Button rejectAndOrder = (Button) dialog.findViewById(R.id.rejectAndOrder);
        rejectAndOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRejectAndReorderListener != null)
                    onRejectAndReorderListener.onRejectAndReorder(order, input.getText().toString());
                dismiss();
            }
        });
        return dialog;
    }
}
