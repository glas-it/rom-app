package ar.com.glasit.rom.Activities;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import ar.com.glasit.rom.R;
import ar.com.glasit.rom.Dialogs.ErrorDialogFragment;
import ar.com.glasit.rom.Dialogs.ErrorDialogFragment.IErrorListener;
import ar.com.glasit.rom.Model.SessionManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class StartSessionActivity extends SherlockFragmentActivity 
							implements IErrorListener{
	
	
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	private static final String errorInvalidCredentials = "Usuario y/o contraseña incorrecta";

	private static final String errorLoginFailed = "Login error";

	// User y password para el login.
	private String userName;
	private String userPass;
	private boolean attemptingLogin;

	// UI .
	private EditText mUserView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_session);
				
		userName = getIntent().getStringExtra(EXTRA_EMAIL);
		mUserView = (EditText) findViewById(R.id.userLogin);
		mUserView.setText(userName);
		mPasswordView = (EditText) findViewById(R.id.passLogin);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.passLogin || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.loginScroll);
		mLoginStatusView = findViewById(R.id.loginLayout);
		mLoginStatusMessageView = (TextView) findViewById(R.id.textLoginLayout);

		findViewById(R.id.loginButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(mLoginStatusView.getWindowToken(), 0);
						attemptLogin();
					}
				});
		
		SessionManager.initialize(getPreferences(0));
		SessionManager.getInstance().retrieveUser();
		
		//valido si ya esta logeado 
		if(SessionManager.getInstance().isSessionOn())	{
	    	Intent intent = new Intent(this, BootstrapActivity.class);
	    	startActivity(intent);
		}

	}

	// intento de logeo
	public void attemptLogin()
	{
		if (attemptingLogin)
		{
			return;
		}
		attemptingLogin = true;
		
		// Resetear errores
		mUserView.setError(null);
		mPasswordView.setError(null);

		// Obtener valores de UI .
		userName = mUserView.getText().toString();
		userPass = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
	
		// Validacion de errores de formato
		if (TextUtils.isEmpty(userPass))
		{
			mPasswordView.setError(getString(R.string.errorFieldIncomplete));
			focusView = mPasswordView;
			cancel = true;
			attemptingLogin=false;
		}
		else if (userPass.length() < 4)
		{
			mPasswordView.setError(getString(R.string.errorTooShortPass));
			focusView = mPasswordView;
			cancel = true;
			attemptingLogin=false;
		}

		if (TextUtils.isEmpty(userName))	{
			mUserView.setError(getString(R.string.errorFieldIncomplete));
			focusView = mUserView;
			cancel = true;
			attemptingLogin=false;
		}
		

		if (cancel)	{
			focusView.requestFocus();
		}
		
		//logear
		else	{
			mLoginStatusMessageView.setText(R.string.loginProcess);
			showProgress(true);
			
			
			//TODO: login services
			Intent intent = new Intent(this, BootstrapActivity.class);
			startActivity(intent);

		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show)
	{

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
		{
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter()
					{
						@Override
						public void onAnimationEnd(Animator animation)
						{
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter()
					{
						@Override
						public void onAnimationEnd(Animator animation)
						{
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		}
		else
		{

			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
		
	}


	public void loginFailed() {
		showProgress(false);
		attemptingLogin = false;

		ErrorDialogFragment dialog = new ErrorDialogFragment();
    	dialog.show(getSupportFragmentManager(), "Error");
	}

	@Override
	public String getErrorMessage() {
		return errorInvalidCredentials;
	}

	@Override
	public String getErrorTitle() {
		return errorLoginFailed;
	}
	
}
