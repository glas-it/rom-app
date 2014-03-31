package ar.com.glasit.rom.Activities;

import android.content.Intent;
import android.os.Bundle;
import ar.com.glasit.rom.Fragments.MenuFragment;
import ar.com.glasit.rom.Fragments.SplashFragment;
import ar.com.glasit.rom.Helpers.ContextHelper;
import ar.com.glasit.rom.Model.SessionManager;
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
        
        getSupportMenuInflater().inflate(R.menu.bootstrap,menu);
//        if (menuResId != 0){
//            getSupportMenuInflater().inflate(menuResId, menu);
//        }
        return  true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SherlockFragment fragment = (SherlockFragment) getLastFragment();
        if (fragment != null) {
            switch (item.getItemId()) {
    		case R.id.close_Session:
    			onCloseSessionButtonTapped();
    	    	return true;
                default:
                    break;
            }
        }
        return true;
    }

    private void onCloseSessionButtonTapped() {
    	SessionManager.getInstance().closeSession();
     	Intent intent = new Intent(this, StartSessionActivity.class);
     	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);	
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
            String stringJson = "[\n" +
                    "    {\n" +
                    "        \"class\": \"rom.Rubro\",\n" +
                    "        \"id\": 1,\n" +
                    "        \"descripcion\": \"Entradas\",\n" +
                    "        \"subrubros\": [\n" +
                    "            {\n" +
                    "                \"class\": \"rom.Subrubro\",\n" +
                    "                \"id\": 2,\n" +
                    "                \"descripcion\": \"Frias\",\n" +
                    "                \"items\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"rom.Item\",\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"title\": \"Ensalada Caesar\",\n" +
                    "                        \"descripcion\": \"Ensalada de pollo, lechuga y salsa caesar\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"rom.Item\",\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"title\": \"Ensalada de mariscos\",\n" +
                    "                        \"descripcion\": \"Ensalada de langostinos y camarones\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"class\": \"rom.Subrubro\",\n" +
                    "                \"id\": 3,\n" +
                    "                \"descripcion\": \"Calientes\",\n" +
                    "                \"items\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"rom.Item\",\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"title\": \"Empanadas\",\n" +
                    "                        \"descripcion\": \"Empanadas de carne fritas\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "        \"class\": \"rom.Rubro\",\n" +
                    "        \"id\": 2,\n" +
                    "        \"descripcion\": \"Postre\",\n" +
                    "        \"subrubros\": [\n" +
                    "            {\n" +
                    "                \"class\": \"rom.Subrubro\",\n" +
                    "                \"id\": 4,\n" +
                    "                \"descripcion\": \"Tortas\",\n" +
                    "                \"items\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"rom.Item\",\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"title\": \"Selva negra\",\n" +
                    "                        \"descripcion\": \"Porcion de selva negra\"\n" +
                    "                    },\n" +
                    "                    {\n" +
                    "                        \"class\": \"rom.Item\",\n" +
                    "                        \"id\": 2,\n" +
                    "                        \"title\": \"Torta de Ricota\",\n" +
                    "                        \"descripcion\": \"Porcion de torta de ricota\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"class\": \"rom.Subrubro\",\n" +
                    "                \"id\": 5,\n" +
                    "                \"descripcion\": \"Tartas\",\n" +
                    "                \"items\": [\n" +
                    "                    {\n" +
                    "                        \"class\": \"rom.Item\",\n" +
                    "                        \"id\": 1,\n" +
                    "                        \"title\": \"Lemon Pie\",\n" +
                    "                        \"descripcion\": \"Porcion de lemon pie\"\n" +
                    "                    }\n" +
                    "                ]\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "]\n";
            JSONArray jsonArray = new JSONArray(stringJson);
            ar.com.glasit.rom.Model.Menu menu = new ar.com.glasit.rom.Model.Menu(jsonArray);
            pushFragment(new MenuFragment(menu));
        } catch (Exception e) {
        }
    }

    private void showMenu(int resId) {
        this.menuResId = resId;
        invalidateOptionsMenu();
    }

}