package ar.com.glasit.rom.Helpers;

import android.content.Context;

public class ContextHelper {
	private static Context context = null;
		private ContextHelper(){
			
		}
		
		public static Context getContextInstance(){
			return context;
		}
		
		public static void setContextInstance(Context ctx){
			context = ctx;
		}
}
