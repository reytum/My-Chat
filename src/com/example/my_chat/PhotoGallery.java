package com.example.my_chat;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PhotoGallery extends Activity {

	@SuppressWarnings("unused")
	private ArrayList<Integer> data = new ArrayList<Integer>();
	@SuppressWarnings("unused")
	private GridView photoGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_gallery);

		LinearLayout rl = (LinearLayout) findViewById(R.id.photo_gallery);
		rl.setBackgroundColor(ThemeOptions.color);
		
		showImages(); 

		photoGrid = (GridView) findViewById(R.id.photoGrid);
	}
	
	public void showImages(){
		Intent gallery = new Intent();
		gallery.setType("images/*");
		gallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(gallery, 0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if ((requestCode == 0) && (resultCode == Activity.RESULT_OK))
		{
			String selectedImage = data.getDataString();
			
			Toast.makeText(PhotoGallery.this, selectedImage, Toast.LENGTH_SHORT).show();;
			
			Intent image = new Intent(Intent.ACTION_VIEW, Uri.parse(selectedImage));
			startActivity(image);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_gallery, menu);
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
