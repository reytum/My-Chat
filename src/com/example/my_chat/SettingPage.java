package com.example.my_chat;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SettingPage extends ListActivity {

	private String[] data = { "Themes", "Profile", "Account Settings", "About", "Help", "Feedback", "Logout" };

	static int color1 = Color.parseColor("#00AFF0");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_page);

		ArrayAdapter<String> aAdap = new ArrayAdapter<String>(SettingPage.this, android.R.layout.simple_list_item_1,
				data);
		setListAdapter(aAdap);

		LinearLayout settingPage = (LinearLayout) findViewById(R.id.settingPage);
		settingPage.setBackgroundColor(ThemeOptions.color);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		switch (position) {
		case 0:
			Intent intent0 = new Intent(SettingPage.this, ThemeOptions.class);
			startActivity(intent0);
			break;

		case 1:
			Intent intent1 = new Intent(SettingPage.this, UserProfile.class);
			startActivity(intent1);
			break;

		case 2:
		case 3:
		case 4:
		case 5:
			Toast.makeText(this, data[position] + " Selected", 0).show();
			break;

		case 6:
			SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putBoolean("isLogged", false);
			editor.commit();
			Intent i = new Intent(SettingPage.this, Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		color1 = ThemeOptions.color;
		LinearLayout settingPage = (LinearLayout) findViewById(R.id.settingPage);
		settingPage.setBackgroundColor(color1);
	}
}
