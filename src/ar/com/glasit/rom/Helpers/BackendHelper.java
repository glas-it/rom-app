package ar.com.glasit.rom.Helpers;

import android.content.Context;
import ar.com.glasit.rom.R;

public class BackendHelper {

    private static final String PREF_NAME = "PREFS_BACKEND";
    private static final String BASE_URL = "BASE_URL";

    private BackendHelper(){
			
    }

    public static String getBackendUrl(){
        String url = ContextHelper.getContextInstance().getString(R.string.backend_url);
        String preferedUrl = ContextHelper.getSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.BASE_URL);
        if (preferedUrl != null && !preferedUrl.isEmpty()) {
            return preferedUrl;
        }
        return url;
    }

    public static void setBackendUrl(String url) {
        ContextHelper.putSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.BASE_URL, url);
    }

}
