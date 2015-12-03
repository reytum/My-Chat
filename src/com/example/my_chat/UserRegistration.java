package com.example.my_chat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class UserRegistration extends Activity implements OnClickListener {

	private int mYear, mMonth, mDay, mHour, mMinute;
	EditText mDate, mTime, fName, lName, uName, password, rPassword, dob, tob, email;
	Spinner country;
	Button button;
	boolean isInserted = false;

	InputStream is = null;

	SQLiteDatabase db;

	public static final String USER_DATA = "com.example.my_chat.data";

	String[] countries = { "", "Australia", "Autria", "Albania", "China", "Belgium", "France", "Italy", "England",
			"Canada", "U.S.A", "Japan", "Pakistan", "South Africa", "India", "Bangladesh", "Srilanka", "U.A.E" };
	protected Locale local = Locale.ENGLISH;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

		setContentView(R.layout.activity_user_registration);

		LinearLayout userRegistration = (LinearLayout) findViewById(R.id.userRegistration);
		userRegistration.setBackgroundColor(ThemeOptions.color);

		mDate = (EditText) findViewById(R.id.datePicker);
		mTime = (EditText) findViewById(R.id.timePicker);

		ActionBar actionBar = getActionBar();
		actionBar.setTitle("Register");

		mDate.setOnClickListener(this);
		mTime.setOnClickListener(this);

		mandatoryFieldCheck();
	}

	protected void mandatoryFieldCheck() {

		button = (Button) findViewById(R.id.sign_up);
		createSpinner();
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!isNetworkAvailable())
					Toast.makeText(UserRegistration.this,
							"No internet connection detected!!! Please make sure you are connected to internet!!! ",
							Toast.LENGTH_SHORT).show();

				else {

					fName = (EditText) findViewById(R.id.first_name);
					lName = (EditText) findViewById(R.id.last_name);
					uName = (EditText) findViewById(R.id.register_user_name);
					email = (EditText) findViewById(R.id.register_email);
					password = (EditText) findViewById(R.id.register_password);
					rPassword = (EditText) findViewById(R.id.register_re_password);
					dob = (EditText) findViewById(R.id.datePicker);
					tob = (EditText) findViewById(R.id.timePicker);

					Spinner spinner = (Spinner) findViewById(R.id.selectCountry);

					final String fName1 = fName.getText().toString().trim();
					final String lName1 = lName.getText().toString().trim();
					final String uName1 = uName.getText().toString().toLowerCase(local).trim();
					final String email1 = email.getText().toString().toLowerCase(local).trim();
					final String password1 = password.getText().toString().trim();
					final String rPassword1 = rPassword.getText().toString().trim();
					final String dob1 = dob.getText().toString();
					final String country1 = spinner.getSelectedItem().toString();
					final String tob1 = tob.getText().toString();

					if ((fName1.length() == 0) || (uName1.length() == 0) || (email1.length() == 0)
							|| (password1.length() == 0) || (rPassword1.length() == 0) || (dob1.length() == 0)
							|| (country1.length() == 0))
						Toast.makeText(UserRegistration.this, "Please Fill the Mandatory Fields First",
								Toast.LENGTH_SHORT).show();

					else if (!password1.equals(rPassword1))
						Toast.makeText(UserRegistration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

					else if (password1.length() < 8)
						Toast.makeText(UserRegistration.this, "Passwords should be minimum 8 characters",
								Toast.LENGTH_SHORT).show();

					else {

						try {

							String urlUname = UserConfig.DATA_URL_UNAME + uName1;
							String urlEmail = UserConfig.DATA_URL_EMAIL + email1;

							UserDataReciever data = new UserDataReciever(UserRegistration.this);
							String userDataUname = (data.execute(urlUname).get())[2];

							UserDataReciever data1 = new UserDataReciever(UserRegistration.this);
							String userDataEmail = (data1.execute(urlEmail).get())[3];

							if (uName1.equals(userDataUname))
								Toast.makeText(UserRegistration.this, "Sorry!! The username is already taken....",
										Toast.LENGTH_SHORT).show();

							else if (email1.equals(userDataEmail))
								Toast.makeText(UserRegistration.this, "Sorry!! The email is already registered....",
										Toast.LENGTH_SHORT).show();
							else {
								new AlertDialog.Builder(UserRegistration.this).setIcon(R.drawable.ic_launcher)
										.setTitle("Warning...").setMessage("Do you want to change the data..?")
										.setPositiveButton("No..My Data is Correct...",
												new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										createNameValuePair(fName1, lName1, uName1, email1, rPassword1, dob1, tob1,
												country1);

										SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);

										SharedPreferences.Editor editor = sp.edit();
										editor.putString("uname", uName1);
										editor.putBoolean("isLogged", true);
										editor.commit();

										Intent i = new Intent(UserRegistration.this, UserProfile.class);
										startActivity(i);
										finish();

									}
								}).setNegativeButton("Yes..", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										Toast.makeText(UserRegistration.this, "Edit the Data.....", Toast.LENGTH_SHORT)
												.show();

									}
								}).setCancelable(false).show();
							}

						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						} catch (IndexOutOfBoundsException e) {
							Toast.makeText(UserRegistration.this, "Newtork Timeout...!!!", Toast.LENGTH_SHORT).show();
						}

					}

				}
			}

		});

	}

	protected void createSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.selectCountry);
		ArrayAdapter<String> arrAdap = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
		arrAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(arrAdap);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void onClick(View v) {
		if (v == mDate) {
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);

			DatePickerDialog dpd = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

					mDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

				}
			}, mYear, mMonth, mDay);
			dpd.show();
		}
		if (v == mTime) {
			final Calendar c = Calendar.getInstance();
			mHour = c.get(Calendar.HOUR_OF_DAY);
			mMinute = c.get(Calendar.MINUTE);

			TimePickerDialog tpd = new TimePickerDialog(this, new OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

					mTime.setText(hourOfDay + ":" + minute);

				}

			}, mHour, mMinute, false);
			tpd.show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_registration, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void createNameValuePair(final String fname, final String lname, final String uname, final String email1,
			final String password1, final String dob1, final String tob1, final String country1) {

		class SendPostRequestAsyncTask extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... params) {

				List<NameValuePair> nvp = new ArrayList<NameValuePair>(1);

				nvp.add(new BasicNameValuePair("user_fname", fname));
				nvp.add(new BasicNameValuePair("user_lname", lname));
				nvp.add(new BasicNameValuePair("user_uname", uname));
				nvp.add(new BasicNameValuePair("user_email", email1));
				nvp.add(new BasicNameValuePair("user_password", password1));
				nvp.add(new BasicNameValuePair("user_dob", dob1));
				nvp.add(new BasicNameValuePair("user_tob", tob1));
				nvp.add(new BasicNameValuePair("user_country", country1));

				try {

					HttpClient httpClient = new DefaultHttpClient();

					HttpPost httpPost = new HttpPost("http://reytum.com/my_chat.php");

					httpPost.setEntity(new UrlEncodedFormEntity(nvp));

					HttpResponse response = httpClient.execute(httpPost);

					HttpEntity entity = response.getEntity();

					is = entity.getContent();

					isInserted = true;

				} catch (ClientProtocolException e) {
					Log.e("Client Protocol", "log_tag");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("IOException", "log_tag");
					e.printStackTrace();
				}

				return "Success";
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				Toast.makeText(UserRegistration.this, "Welcome", Toast.LENGTH_SHORT).show();
			}

		}
		SendPostRequestAsyncTask sendPostReqAsyncTask = new SendPostRequestAsyncTask();
		sendPostReqAsyncTask.execute(fname, lname, uname, email1, password1, dob1, tob1, country1);
	}

	public boolean isNetworkAvailable() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();

	}

}
