package com.example.my_chat;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class SendMessage extends Activity {

	Button openGallery;
	ArrayList<String> messages = new ArrayList<String>();
	EditText typedMessage;
	Button send;
	ListView messageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message);

		Bundle bundle = getIntent().getExtras();

		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_launcher);
		actionBar.setTitle(bundle.getString("fullName"));
		actionBar.setSubtitle(bundle.getString("uname"));

		setMessageList();

	}

	public void setMessageList() {
		typedMessage = (EditText) findViewById(R.id.message);
		send = (Button) findViewById(R.id.send_message);
		messageList = (ListView) findViewById(R.id.messageList);

		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String m = typedMessage.getText().toString().trim();
				if (!m.isEmpty()) {
					typedMessage.getText().clear();
					messages.add("You : " + m);
					ArrayAdapter<String> adap = new ArrayAdapter<String>(SendMessage.this,
							android.R.layout.simple_list_item_1, messages);
					messageList.setAdapter(adap);
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.send_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.open_gallery) {
			Intent intent = new Intent(SendMessage.this, PhotoGallery.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}
}
