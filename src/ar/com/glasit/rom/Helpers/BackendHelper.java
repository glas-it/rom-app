package ar.com.glasit.rom.Helpers;

import ar.com.glasit.rom.R;

public class BackendHelper {

    private static final String PREF_NAME = "PREFS_BACKEND";
    private static final String BASE_URL = "BASE_URL";
    private static final String USER = "USER";
    private static final String KEY = "KEY";
    private static final String APP_TYPE = "APP_TYPE";
    private static final String COMPANY = "COMPANY";

    private BackendHelper(){}

    public static String getBackendUrl(){
        String url = ContextHelper.getContextInstance().getString(R.string.backend_url);
        String preferedUrl = ContextHelper.getSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.BASE_URL);
        if (preferedUrl != null && !preferedUrl.isEmpty()) {
            return preferedUrl;
        }
        return url;
    }

    public static String getCompany(){
        return ContextHelper.getSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.COMPANY);
    }

    public static void setCompany(String company){
        ContextHelper.putSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.COMPANY, company);
    }

    public static String getSecretKey(){
        return ContextHelper.getSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.KEY);
    }

    public static String getLoggedUser(){
        return ContextHelper.getSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.USER);
    }

    public static void setBackendUrl(String url) {
        ContextHelper.putSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.BASE_URL, url);
    }

    public static void setSecretKey(String key) {
        ContextHelper.putSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.KEY, key);
    }

    public static void setAppType(String key) {
        ContextHelper.putSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.APP_TYPE, key);
    }

    public static String getsetAppType(){
        return ContextHelper.getSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.APP_TYPE);
    }

    public static void setLoggedUser(String user) {
        ContextHelper.putSharedPrefenrece(BackendHelper.PREF_NAME, BackendHelper.USER, user);
    }

}
