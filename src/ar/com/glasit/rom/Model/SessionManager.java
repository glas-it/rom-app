package ar.com.glasit.rom.Model;

import java.util.UUID;

import android.content.SharedPreferences;

public class SessionManager {

	private static final String userSession = "userSession";
	private static final String userId = "userId";
	private static final String userPass = "userPass";
	private static final String userName = "userName";
	private User user;
	private boolean sessionOn;
	private  SharedPreferences settings;
	
	private static SessionManager session;

	private  SessionManager (SharedPreferences pref) {
		settings= pref;
	}
	
	public static void initialize(SharedPreferences settings) {
		session = new SessionManager(settings);
	}
	
	public static SessionManager getInstance() {
		return session;
	}
	
	public void setUser(User user) {
		sessionOn = true;
		this.user = user;
		
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(userName, user.getUser());
		editor.putString(userPass, user.getPassword());
//		editor.putString(userId, user.getUserId().toString());
		editor.putBoolean(userSession, true);
		editor.commit();
	}
	
	public String getUser() {
		return user.getUser();
	}
	
	public void retrieveUser()
	{
		sessionOn = settings.getBoolean(userSession, false);
		if(sessionOn)	{
			String uName, uPass, uId;
			uName = settings.getString(userName, "");
			uPass = settings.getString(userPass, "");
//			uId = settings.getString(userId, "");
			
			if(0 == uName.length() || 0 == uPass.length())	{
				sessionOn = false;
			}
			else
			{
				this.user = new User(uName, uPass);
//				this.user.setUserId(UUID.fromString(uId));
			}
		}
	}

	public String getPassword()	{
		return user.getPassword();
	}
	
	public boolean isSessionOn() {
		return sessionOn;
	}

	public UUID getUserId() {
		return user.getUserId();
	}
	
	public void closeSession() {
		sessionOn = false;
		this.user = null;
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(userName, "");
		editor.putString(userPass, "");
//		editor.putString(userId, "");
		editor.putBoolean(userSession, false);
		editor.commit();
		
	}
}
