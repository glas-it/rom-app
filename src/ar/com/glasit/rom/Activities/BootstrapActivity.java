package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;

import ar.com.glasit.rom.Fragments.ItemFragment;
import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Fragments.SplashFragment;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.IItem;
import ar.com.glasit.rom.Model.OnSelectItemListener;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

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