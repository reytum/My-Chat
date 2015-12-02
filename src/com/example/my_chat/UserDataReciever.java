package com.example.my_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class UserDataReciever extends AsyncTask<String, Void, String[]> {

	static String[] userData = new String[8];
	Context context;
	ProgressDialog progress;

	public UserDataReciever(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		progress = ProgressDialog.show(context, "Please Wait....", "Fetching Data...");
		super.onPreExecute();

	}

	@Override
	protected String[] doInBackground(String... params) {

		HttpURLConnection conn = null;
		BufferedReader reader = null;

		try {
			URL url = new URL(params[0]);
			conn = (HttpURLConnection) url.openConnection();
			conn.connect();

			InputStream stream = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(stream));
			StringBuffer buffer = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			JSONObject jObj = new JSONObject(buffer.toString());
			JSONArray jArray = jObj.getJSONArray(UserConfig.JSON_ARRAY);
			JSONObject userDataObj = jArray.getJSONObject(0);

			userData[0] = userDataObj.getString(UserConfig.FNAME);
			userData[1] = userDataObj.getString(UserConfig.LNAME);
			userData[2] = userDataObj.getString(UserConfig.UNAME);
			userData[3] = userDataObj.getString(UserConfig.EMAIL);
			userData[4] = userDataObj.getString(UserConfig.PASSWORD);
			userData[5] = userDataObj.getString(UserConfig.DOB);
			userData[6] = userDataObj.getString(UserConfig.TOB);
			userData[7] = userDataObj.getString(UserConfig.COUNTRY);
			
			return userData;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {

			if (conn != null)
				conn.disconnect();
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onPostExecute(String[] result) {
		super.onPostExecute(result);	
		progress.dismiss();
	}

}
