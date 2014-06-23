package ar.com.glasit.rom.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.glasit.rom.Activities.SetupListener;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragment;

public class SetUpFragment extends SherlockFragment{

    private Spinner appType;
    private EditText restaurantKey;
    private SetupListener listener;

    public SetUpFragment(SetupListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_session, container,
                false);

        appType = (Spinner) view.findViewById(R.id.applicationType);
        restaurantKey = (EditText) view.findViewById(R.id.restaurantKey);
        restaurantKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = restaurantKey.getText().toString();
                if (text.equals("SecretKey")) {
                    restaurantKey.setText("");
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
                    restaurantKey.setText("");
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
                    restaurantKey.setText("");
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
                    restaurantKey.setText("");
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
        Button btn = (Button) view.findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurantKey.getText().toString().isEmpty()) {
                    Toast.makeText(getSherlockActivity(), getText(R.string.empty_restaurant_key),Toast.LENGTH_SHORT).show();
                    return;
                }
                BackendHelper.setSecretKey(restaurantKey.getText().toString());
                BackendHelper.setAppType(appType.getSelectedItem().toString());
                if (appType.getSelectedItem().equals("Cocina")) {
                    BackendHelper.setLoggedUser("cocina");
                } else if (appType.getSelectedItem().equals("Barra")) {
                    BackendHelper.setLoggedUser("barra");
                }
                listener.setupSuccess();
            }
        });
        return view;
    }



}
