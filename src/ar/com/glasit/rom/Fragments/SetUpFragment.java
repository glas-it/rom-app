package ar.com.glasit.rom.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ar.com.glasit.rom.Activities.BootstrapActivity;
import ar.com.glasit.rom.Activities.MenuActivity;
import ar.com.glasit.rom.Activities.SetUpActivity;
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
        Button btn = (Button) view.findViewById(R.id.save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (restaurantKey.getText().toString().isEmpty()) {
                    Toast.makeText(getSherlockActivity(), getText(R.string.empty_restaurant_key),Toast.LENGTH_SHORT).show();
                }
                BackendHelper.setSecretKey(restaurantKey.getText().toString());
                BackendHelper.setAppType(appType.getSelectedItem().toString());
                listener.setupSuccess();
            }
        });
        return view;
    }



}
