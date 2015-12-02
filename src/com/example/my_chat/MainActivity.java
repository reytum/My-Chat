package com.example.my_chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {

	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);

		sp = getSharedPreferences("User", Context.MODE_PRIVATE);

		if (sp != null && String.valueOf(sp.getBoolean("isLogged", false)) == "true") {
			Intent intent = new Intent(MainActivity.this, FriendList.class);
			startActivity(intent);
			finish();
		} else

		{
			Intent intent = new Intent(MainActivity.this, Login.class);
			startActivity(intent);
			finish();
		}
	}

}