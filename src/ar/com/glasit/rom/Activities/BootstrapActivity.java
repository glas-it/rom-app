package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;

import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.R;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class BootstrapActivity extends SherlockFragmentActivity{

    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextHelper.setContextInstance(this.getApplicationContext());
        if (savedInstanceState == null) {
            if (isFirstTime()){
                startSetup();
            } else if (!isLoggedIn()) {
                startLoginUser();
            } else {
                String type = BackendHelper.getAppType();
                if (type.equals("Mozo")) {
                    startTables();
                } else if (type.equals("Cocina")){
                    startKitchen();
                } else {
                    startBar();
                }
            }
        }
    }

    private boolean isLoggedIn(){
        return !BackendHelper.getLoggedUser().isEmpty();
    }

    private boolean isFirstTime(){
        return BackendHelper.getAppType().isEmpty();
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

    private void startSetup() {
        startActivity(new Intent(this, SetUpActivity.class));
        finish();
    }
    
    private void startTables() {
        startActivity(new Intent(this, TablesActivity.class));
        finish();
    }

    private void startKitchen() {
        Intent intent = new Intent(this, KitchenActivity.class);
        intent.putExtra("type", "KITCHEN");
        startActivity(intent);
        finish();
    }

    private void startBar() {
        Intent intent = new Intent(this, KitchenActivity.class);
        intent.putExtra("type", "BAR");
        startActivity(intent);
        finish();
    }
}