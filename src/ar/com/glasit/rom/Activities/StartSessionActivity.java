package ar.com.glasit.rom.Activities;

import android.content.Intent;
import ar.com.glasit.rom.Fragments.StartSessionFragment;
import ar.com.glasit.rom.Model.LoginListener;
import ar.com.glasit.rom.R;
import android.os.Bundle;

public class StartSessionActivity extends StackFragmentActivity implements LoginListener{

    StartSessionFragment fragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new StartSessionFragment(this);
        addFragment(fragment);
    }

    @Override
    public void loginSuccess() {
        getSupportActionBar().show();
        startActivity(new Intent(this, TablesActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragment != null) {
            fragment.setLoginListener(this);
        }
    }
}
