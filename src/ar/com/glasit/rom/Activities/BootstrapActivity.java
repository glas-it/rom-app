package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;

import ar.com.glasit.rom.Fragments.MenuFragment;
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
                showSplash();
            } else {
                startApp();
            }
        }
    }

    private void onCloseSessionButtonTapped() {
    	SessionManager.getInstance().closeSession();
     	Intent intent = new Intent(this, StartSessionActivity.class);
     	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);	
	}
    
    private boolean isFirstTime(){
        return false;
    }

    private void showSplash() {
        getSupportActionBar().hide();
        //getSupportFragmentManager().beginTransaction().add(R.id.container, new SplashFragment()).commit();
        startLoginUser();
    }

    private void startLoginUser() {
        // TODO:
    }

    private void startApp() {
        getSupportActionBar().show();
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }
}