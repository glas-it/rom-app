package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;

import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Model.SessionManager;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class BootstrapActivity extends SherlockFragmentActivity{

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextHelper.setContextInstance(this.getApplicationContext());
        if (savedInstanceState == null) {
            if (isFirstTime()){
                startLoginUser();
            } else {
                startApp();
            }
        }
    }

    private boolean isFirstTime(){
        return BackendHelper.getLoggedUser().isEmpty();
    }

    private void startLoginUser() {
        getSupportActionBar().show();
        startActivity(new Intent(this, StartSessionActivity.class));
        finish();
    }

    private void startApp() {
        getSupportActionBar().show();
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}