package ar.com.glasit.rom.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class ContextHelper {
	private static Context context = null;

	private ContextHelper(){}
		
    public static Context getContextInstance(){
        return context;
    }

    public static void setContextInstance(Context ctx){
        context = ctx;
    }

    public static String getSharedPrefenrece(String prefrencesName, String key){
        SharedPreferences shared = context.getSharedPreferences(prefrencesName, context.MODE_PRIVATE);
        return shared.getString(key, "");
    }

    public static void putSharedPrefenrece(String prefrencesName, String key, String data){
        SharedPreferences shared = context.getSharedPreferences(prefrencesName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, data);
        editor.commit();
    }

}
