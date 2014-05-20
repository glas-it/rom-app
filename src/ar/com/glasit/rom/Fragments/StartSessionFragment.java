package ar.com.glasit.rom.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Model.LoginListener;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Service.*;
import com.devspark.progressfragment.SherlockProgressFragment;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.List;
import java.util.Vector;

public class StartSessionFragment extends SherlockProgressFragment{

    private View fragView;

    private EditText mUserView;
    private LoginListener loginListener;
    public StartSessionFragment(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public StartSessionFragment() {
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!BackendHelper.getCompany().isEmpty()){
            getSherlockActivity().getSupportActionBar().setTitle(BackendHelper.getCompany());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(fragView);
        setContentShown(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragView = inflater.inflate(R.layout.fragment_start_session, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserView = (EditText) fragView.findViewById(R.id.userLogin);
        fragView.findViewById(R.id.loginButton).setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });

        mUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mUserView.getText().toString();
                if (text.equals("SecretKey")) {
                    mUserView.setText("");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                    final EditText input = new EditText(getSherlockActivity());
                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            BackendHelper.setSecretKey(input.getText().toString());
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                } else if (text.equals("UserName")){
                    mUserView.setText("");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                    final EditText input = new EditText(getSherlockActivity());
                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            BackendHelper.setLoggedUser(input.getText().toString());
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                } else if (text.equals("BaseUrl")){
                    mUserView.setText("");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                    final EditText input = new EditText(getSherlockActivity());
                    input.setText(BackendHelper.getBackendUrl());
                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            BackendHelper.setBackendUrl(input.getText().toString());
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                } else if (text.equals("CompanyName")){
                    mUserView.setText("");
                    AlertDialog.Builder alert = new AlertDialog.Builder(getSherlockActivity());
                    final EditText input = new EditText(getSherlockActivity());
                    input.setText(BackendHelper.getCompany());
                    alert.setView(input);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            BackendHelper.setCompany(input.getText().toString());
                            String name = BackendHelper.getCompany();
                            getSherlockActivity().getSupportActionBar().setTitle((name.isEmpty()) ? getString(R.string.app_name): name);
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
                    alert.show();
                }
            }
        });
    }

    public void attemptLogin()
    {
        String userName = mUserView.getText().toString();
        String userKey = BackendHelper.getSecretKey();

        if (userName.isEmpty()) {
            showError(getString(R.string.errorFieldIncomplete));
        } else {
            List<NameValuePair> nameValuePairs = new Vector<NameValuePair>();
            setContentShown(false);
            RestService.callGetService(serviceListener, WellKnownMethods.Login + userName + "/" + userKey, nameValuePairs);
        }
    }

    private void showError(String message) {
        Toast.makeText(getSherlockActivity(), message, Toast.LENGTH_SHORT).show();
    }

    ServiceListener serviceListener = new ServiceListener() {
        @Override
        public void onServiceCompleted(IServiceRequest request, ServiceResponse obj) {
            setContentShown(true);
            try {
                if (obj.getSuccess()) {
                    BackendHelper.setLoggedUser(mUserView.getText().toString());
                    loginListener.loginSuccess();
                    try {
                        BackendHelper.setLoggedUserName(obj.getJsonObject().getString("nombre"));
                    } catch (Exception e) {}
                } else {
                    showError(getString(R.string.errorMail));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(String error) {
            setContentShown(true);
            showError(getString(R.string.connectivity_error));
        }
    };
}
