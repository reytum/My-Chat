package com.example.my_chat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity {

	Button startChat;
	TextView fullName, username, tvEmail, dob_tob, tvCountry;

	String uname, unameRec = null;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

		fullName = (TextView) findViewById(R.id.full_name);
		username = (TextView) findViewById(R.id.profile_uname);
		tvEmail = (TextView) findViewById(R.id.profile_email);
		dob_tob = (TextView) findViewById(R.id.profile_dob_tob);
		tvCountry = (TextView) findViewById(R.id.profiile_country);

		SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
		try {
			Intent intent = getIntent();
			Bundle bundle = intent.getExtras();
			unameRec = bundle.getString("username");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (unameRec == null) {
			if (String.valueOf(sp.getBoolean("isLogged", false)) == "true")
				uname = sp.getString("uname", "Nothing Exists...");
		}

		else

			uname = unameRec;

		final ProgressDialog loading = ProgressDialog.show(UserProfile.this, "Please wait...", "Fetching...", false,
				false);

		String url = UserConfig.DATA_URL_UNAME + uname;

		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONArray result = jsonObject.getJSONArray(UserConfig.JSON_ARRAY);
					JSONObject userDataObj = result.getJSONObject(0);

					String[] userData = new String[8];

					userData[0] = userDataObj.getString(UserConfig.FNAME);
					userData[1] = userDataObj.getString(UserConfig.LNAME);
					userData[2] = userDataObj.getString(UserConfig.UNAME);
					userData[3] = userDataObj.getString(UserConfig.EMAIL);
					userData[4] = userDataObj.getString(UserConfig.PASSWORD);
					userData[5] = userDataObj.getString(UserConfig.DOB);
					userData[6] = userDataObj.getString(UserConfig.TOB);
					userData[7] = userDataObj.getString(UserConfig.COUNTRY);

					String tob1 = "";

					fullName.setText(userData[0] + " " + userData[1]);

					username.setText(userData[2]);
					tvEmail.setText(userData[3]);
					if (!userData[6].equals(""))
						tob1 = "(" + userData[6] + ")";

					dob_tob.setText(userData[5] + tob1);
					tvCountry.setText(userData[7]);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				loading.dismiss();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(UserProfile.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
			}
		});

		RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.this);
		requestQueue.add(stringRequest);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
