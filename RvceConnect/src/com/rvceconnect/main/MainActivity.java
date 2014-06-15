package com.rvceconnect.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main_activity);
		Toast.makeText(this, "Welcome...!!!", Toast.LENGTH_LONG).show();
		
		// start tracing to "[/sdcard/]jWebSocketAndroidDemo.trace"
		// Debug.startMethodTracing("/sdcard/jws");

		JWC.init();
		JWC.loadSettings(this);
		
		String[] lItems = {"Chat Screen", "Initial Setup"};
		ListView lv = (ListView)findViewById(R.id.listView1);
		lv.invalidateViews();
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lItems));

		//ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
					case 0:
						startActivity(new Intent(MainActivity.this, ChatScreen.class));
						break;
					case 1:
						startActivity(new Intent(MainActivity.this, ConfigActivity.class));
						break;
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// stop tracing
		// Debug.stopMethodTracing();
		super.onDestroy();
		
		}
}
