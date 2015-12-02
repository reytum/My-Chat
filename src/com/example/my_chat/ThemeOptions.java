package com.example.my_chat;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ThemeOptions extends Activity {

	String[] theme = { "Default", "Theme 1", "Theme 2", "Theme 3", "Theme 4", "Theme 5", "Theme 6", "Theme 7",
			"Theme 8" };

	private GridView gridView;
	private ListView listView;
	static int color = Color.parseColor("#00AFF0");

	public void gridView() {

		gridView = new GridView(this);
		LinearLayout ll = (LinearLayout) findViewById(R.id.themeOptions);

		gridView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		gridView.setNumColumns(3);
		gridView.setVerticalSpacing(5);
		gridView.setPadding(10, 10, 10, 10);

		ArrayAdapter<String> arrAdap = new ArrayAdapter<String>(ThemeOptions.this, android.R.layout.simple_list_item_1,
				theme);
		gridView.setAdapter(arrAdap);

		ll.addView(gridView);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Toast.makeText(ThemeOptions.this, theme[arg2] + " Selected", 0).show();

				switch (arg2) {
				case 0:
					color = Color.parseColor("#00AFF0");
					break;
				case 1:
					color = Color.parseColor("#093DA3");
					break;
				case 2:
					color = Color.parseColor("#A61395");
					break;
				case 3:
					color = Color.parseColor("#BE240C");
					break;
				case 4:
					color = Color.parseColor("#21B41E");
					break;
				case 5:
					color = Color.parseColor("#B8A80B");
					break;
				case 6:
					color = Color.parseColor("#9C1E8A");
					break;
				case 7:
					color = Color.parseColor("#85732E");
					break;
				case 8:
					color = Color.parseColor("#BD8B87");
					break;

				}
				LinearLayout themeOptions = (LinearLayout) findViewById(R.id.themeOptions);
				themeOptions.setBackgroundColor(color);
			}
		});
	}

	public void listView() {
		listView = new ListView(this);
		LinearLayout ll = (LinearLayout) findViewById(R.id.themeOptions);

		ArrayAdapter<String> arrAdap = new ArrayAdapter<String>(ThemeOptions.this, android.R.layout.simple_list_item_1,
				theme);
		listView.setAdapter(arrAdap);

		ll.addView(listView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Toast.makeText(ThemeOptions.this, theme[arg2] + " Selected", 0).show();

				switch (arg2) {
				case 0:
					color = Color.parseColor("#00AFF0");
					break;
				case 1:
					color = Color.parseColor("#093DA3");
					break;
				case 2:
					color = Color.parseColor("#A61395");
					break;
				case 3:
					color = Color.parseColor("#BE240C");
					break;
				case 4:
					color = Color.parseColor("#21B41E");
					break;
				case 5:
					color = Color.parseColor("#B8A80B");
					break;
				case 6:
					color = Color.parseColor("#9C1E8A");
					break;
				case 7:
					color = Color.parseColor("#85732E");
					break;
				case 8:
					color = Color.parseColor("#BD8B87");
					break;

				}
				LinearLayout themeOptions = (LinearLayout) findViewById(R.id.themeOptions);
				themeOptions.setBackgroundColor(color);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_theme_options);
		gridView();
		final LinearLayout themeOptions = (LinearLayout) findViewById(R.id.themeOptions);
		themeOptions.setBackgroundColor(SettingPage.color1);

		ImageButton list = (ImageButton) findViewById(R.id.listView);
		ImageButton grid = (ImageButton) findViewById(R.id.gridView2);

		list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				themeOptions.removeAllViews();
				setContentView(R.layout.activity_theme_options);
				listView();

			}
		});

		grid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				themeOptions.removeAllViews();
				setContentView(R.layout.activity_theme_options);
				gridView();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.theme_options, menu);
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
}
