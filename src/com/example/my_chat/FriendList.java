package com.example.my_chat;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class FriendList extends Activity {

	ArrayList<FriendListData> friendsDetails = new ArrayList<FriendListData>();
	String unameLogged;
	SQLiteDatabase db;
	String dbName;
	ListView friendList;
	String friendFullName, friendUname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);

		friendList = (ListView) findViewById(R.id.friendList);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

		SharedPreferences sp = getSharedPreferences("User", Context.MODE_PRIVATE);
		if (String.valueOf(sp.getBoolean("isLogged", false)) == "true")
			unameLogged = sp.getString("uname", "Nothing Exists...");

		dbName = unameLogged + "_FriendList";

		db = openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS FriendList(uname TEXT,fname TEXT,lname TEXT);");
		onCreateHelper();

	}

	public void onCreateHelper() {

		final Cursor c = db.rawQuery("SELECT * FROM FriendList", null);

		if (c.getCount() == 0)
			Toast.makeText(FriendList.this, "Please add some friends to start a chat", Toast.LENGTH_SHORT).show();

		else {

			while (c.moveToNext()) {
				FriendListData data = new FriendListData();
				friendFullName = c.getString(1) + " " + c.getString(2);
				data.setFullName(friendFullName);
				friendUname = c.getString(0);
				data.setUname(friendUname);
				friendsDetails.add(data);
			}

			CustomFriendListAdapter adap = new CustomFriendListAdapter(FriendList.this, friendsDetails);
			friendList.setAdapter(adap);

			registerForContextMenu(friendList);

			friendList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					FriendListData data = friendsDetails.get(arg2);

					Bundle bundle = new Bundle();
					Intent i = new Intent(FriendList.this, SendMessage.class);
					bundle.putString("fullName", data.fullName);
					bundle.putString("uname", data.uname);
					i.putExtras(bundle);
					startActivity(i);

				}

			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.add_friend) {

			final EditText searchUname = new EditText(this);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Enter the Username");
			builder.setView(searchUname);

			builder.setPositiveButton("Add", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {

					final String friendUname = searchUname.getText().toString().trim();

					Cursor c = db.rawQuery("SELECT * FROM FriendList WHERE uname='" + friendUname + "'", null);

					if (friendUname.length() == 0)
						Toast.makeText(FriendList.this, "Please enter a Username!!!!", Toast.LENGTH_SHORT).show();

					else if (friendUname.equals(unameLogged))
						Toast.makeText(FriendList.this, "You cannot chat with yourself", Toast.LENGTH_SHORT).show();

					else if (c.moveToNext())
						Toast.makeText(FriendList.this,
								c.getString(1) + " " + c.getString(2) + " already exists in your friend list...",
								Toast.LENGTH_SHORT).show();

					else {
						final ProgressDialog loading = ProgressDialog.show(FriendList.this, "Please Wait",
								"Searching....");

						String url = UserConfig.DATA_URL_UNAME + friendUname;

						StringRequest req = new StringRequest(url, new Listener<String>() {

							@Override
							public void onResponse(String response) {

								try {
									JSONObject obj = new JSONObject(response);
									JSONArray arr = obj.getJSONArray(UserConfig.JSON_ARRAY);
									final JSONObject jObj = arr.getJSONObject(0);

									String res = jObj.getString(UserConfig.UNAME);

									if (res.equals("null"))
										Toast.makeText(FriendList.this, "Sorry! The username entered is invalid",
												Toast.LENGTH_SHORT).show();

									else {

										try {
											db.execSQL("INSERT INTO FriendList(uname, fname, lname) VALUES('"
													+ friendUname + "','" + jObj.getString(UserConfig.FNAME) + "','"
													+ jObj.getString(UserConfig.LNAME) + "');");
										} catch (SQLiteException e) {
											Toast.makeText(FriendList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
										}

										recreate();

										Toast.makeText(FriendList.this, "Friend added succesfully!!!!!!",
												Toast.LENGTH_SHORT).show();

									}
								} catch (JSONException e) {
									e.printStackTrace();
								} catch (NullPointerException e) {
									Toast.makeText(FriendList.this, "Network Timeout!!!", Toast.LENGTH_SHORT).show();
								}

								loading.dismiss();

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								Toast.makeText(FriendList.this, error.getMessage().toString(), Toast.LENGTH_LONG)
										.show();
							}
						});

						RequestQueue requestQueue = Volley.newRequestQueue(FriendList.this);
						requestQueue.add(req);

					}
				}

			});

			builder.setNegativeButton("Cancel", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.setCancelable(false);
			builder.show();

		} else if (id == R.id.action_setting)

		{
			Intent intent = new Intent(FriendList.this, SettingPage.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.friendList) {

			FriendListData data = new FriendListData();
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			data = friendsDetails.get(info.position);
			String name = data.getFullName();
			menu.setHeaderTitle(name);
			menu.add(0, 0, 0, "View Profile");
			menu.add(0, 1, 1, "Delete Chat");
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		FriendListData data = new FriendListData();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		data = friendsDetails.get(info.position);
		final String uname = data.getUname();
		int itemId = item.getItemId();
		if (itemId == 0) {
			Intent i = new Intent(FriendList.this, UserProfile.class);
			Bundle bundle = new Bundle();
			bundle.putString("username", uname);
			i.putExtras(bundle);
			startActivity(i);
		} else if (itemId == 1) {
			new AlertDialog.Builder(FriendList.this).setTitle("Delete " + data.getFullName())
					.setMessage("Are you sure, All the messages will be deleted..")
					.setNegativeButton("Yes", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							db.execSQL("DELETE FROM FriendList WHERE uname='" + uname + "'");
							recreate();
						}
					}).setPositiveButton("No", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).show();

		}
		return super.onContextItemSelected(item);
	}

}
