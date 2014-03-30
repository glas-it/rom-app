package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;

import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Fragments.SplashFragment;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONObject;

public class BootstrapActivity extends StackFragmentActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
    private int menuResId = 0;
    private boolean disableBack;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextHelper.setContextInstance(this.getApplicationContext());
        disableBack = false;
        if (savedInstanceState == null) {
            if (isFirstTime()){
                showSplash();
            } else {
                startApp();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (menuResId != 0){
            getSupportMenuInflater().inflate(menuResId, menu);
        }
        return  true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if  (!disableBack) {
            SherlockFragment fragment = (SherlockFragment) getLastFragment();
            if (fragment != null) {
                ((MenuFragment) fragment).onBackPressed();
            } else {
                super.onBackPressed();
            }
        }
    }

    private boolean isFirstTime(){
        return false;
    }

    private void showSplash() {
        getSupportActionBar().hide();
        pushFragment(new SplashFragment());
    }

    private void startLoginUser() {
        // TODO:
    }

    private void startApp() {
        getSupportActionBar().show();
/*        startActivity(new Intent(this, MainActivity.class));
        finish();*/
        try {
            pushFragment(new MenuFragment());
        } catch (Exception e) {
        }
    }

    private void showMenu(int resId) {
        this.menuResId = resId;
        invalidateOptionsMenu();
    }

}