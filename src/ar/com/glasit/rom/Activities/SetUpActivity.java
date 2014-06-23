package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;
import ar.com.glasit.rom.Fragments.SetUpFragment;
import ar.com.glasit.rom.R;

public class SetUpActivity extends StackFragmentActivity implements SetupListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(new SetUpFragment(this));
    }

    @Override
    public void setupSuccess() {
        startActivity(new Intent(this, BootstrapActivity.class));
        finish();
    }
}
