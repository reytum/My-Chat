package com.example.my_chat;

import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	SQLiteDatabase db;
	SharedPreferences sp;

	Button login;
	EditText etPwd;
	CheckBox cbShowPwd;
	String pwd, pw;
	String email, em;
	int flag;
	ProgressDialog progress;
	private AutoCompleteTextView auto;
	private String[] data = { "reytum", "reytum@gmail.com", "reytum@live.com", "reytum@aol.com", "reytum007@yahoo.com",
			"rey.tum1", "reytum@reytum.com", "reytum@engineer.com", "reytum@musician.org", "reytum007@gmail.com" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

		setEmailField();

		LinearLayout loginPage = (LinearLayout) findViewById(R.id.login_page);
		loginPage.setBackgroundColor(ThemeOptions.color);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Login Page");

		etPwd = (EditText) findViewById(R.id.password);
		cbShowPwd = (CheckBox) findViewById(R.id.checkBox);

		cbShowPwd.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (!isChecked) {
					etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {
					etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
			}
		});

		TextView registration = (TextView) findViewById(R.id.register);

		registration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Login.this, UserRegistration.class);
				startActivity(intent);
			}
		});

		TextView forgotPassword = (TextView) findViewById(R.id.forgotPassword);

		forgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(Login.this, ForgotPassword.class);
				startActivity(intent);

			}
		});

		sp = getSharedPreferences("User", Context.MODE_PRIVATE);
		clickLogin();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		LinearLayout loginPage = (LinearLayout) findViewById(R.id.login_page);
		loginPage.setBackgroundColor(ThemeOptions.color);
	}

	public boolean isNetworkAvailable() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();

	}

	public void clickLogin()

	{
		login = (Button) findViewById(R.id.login);

		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!isNetworkAvailable())
					Toast.makeText(Login.this,
							"No internet connection detected!!! Please make sure you are connected to internet!!! ",
							Toast.LENGTH_SHORT).show();
				else
					try {
						validateLogin();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}

			}
		});

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(Login.this).setTitle("Exit My-Chat").setMessage("Are you sure you want to quit?")
				.setNegativeButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setPositiveButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();

	}

	public void setEmailField() { // AutoCompleteTextView
		auto = (AutoCompleteTextView) findViewById(R.id.email);
		ArrayAdapter<String> arrAdap = new ArrayAdapter<String>(Login.this, android.R.layout.simple_dropdown_item_1line,
				data);
		auto.setAdapter(arrAdap);

		auto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void validateLogin() throws InterruptedException, ExecutionException {

		final AutoCompleteTextView emailText = (AutoCompleteTextView) findViewById(R.id.email);
		final EditText pwdEdit = (EditText) findViewById(R.id.password);

		em = emailText.getText().toString().trim();
		String pwEt = pwdEdit.getText().toString().trim();

		if (em.length() == 0)
			emailText.setError("Username Field Cannot be Blank");

		else if (pwEt.length() == 0)
			pwdEdit.setError("Please Enter the Password");

		else {

			String fname, lname;

			UserDataReciever data = new UserDataReciever(Login.this);
			String[] userData = data.execute(UserConfig.DATA_URL_UNAME + em).get();
			pw = userData[4];
			fname = userData[0];
			lname = userData[1];

			if (pw.equals("null"))
				emailText.setError("Incorrect Username or Email");

			else if (!pw.equals(pwEt))
				pwdEdit.setError("Incorrect Password");

			else {

				View toast = getLayoutInflater().inflate(R.layout.custom_toast,
						(ViewGroup) findViewById(R.id.linearLayout));
				TextView toastText = (TextView) toast.findViewById(R.id.textView2);

				Toast t = new Toast(Login.this);
				t.setDuration(Toast.LENGTH_LONG);
				t.setView(toast);
				t.setGravity(Gravity.CENTER, 0, 0);
				toastText.setText(fname + " " + lname);
				t.show();

				SharedPreferences.Editor editor = sp.edit();
				editor.putString("uname", em);
				editor.putBoolean("isLogged", true);
				editor.commit();
				Intent intent = new Intent(Login.this, FriendList.class);
				startActivity(intent);
				finish();

			}
		}

	}
}
