package com.example.basicphotobrowser;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// where to look for photos when the app is launched
	final String STARTUP_DIRECTORY = Environment.getExternalStorageDirectory() + "/Pictures/Pictures/";
	final String IP_ADDRESS_OF_SURFACE = "10.6.6.176"; // Surface in PITLab has IP: 10.6.6.175
	final int PORT = 3000;

	// used to change the folder in configuration changes
	final String SELECTED_FOLDER_KEY = "SELECTED_FOLDER_KEY";
	String selectedFolder;

	final String PREVIOUS_FOLDER_KEY = "PREVIOUS_FOLDER_KEY";
	String previousFolder;

	GridView gridview;
	ImageAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Intent serviceIntent = new Intent(getBaseContext(), ImageServerService.class);
		startService(serviceIntent);
		
		setContentView(R.layout.activity_main);



		gridview = (GridView) findViewById(R.id.gridview);

		// the onClick part does almost nothing currently
		// but should ideally send the photo to the PixelSense or open detail view
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				String filePath = adapter.getItem(position);
				File f = new File(filePath);

				// send the file to the Surface/PixelSense table
				ImageSender sender = new ImageSender(getBaseContext(), IP_ADDRESS_OF_SURFACE, PORT);
				sender.execute(f);
				Toast.makeText(MainActivity.this, "Sending file...", Toast.LENGTH_SHORT).show();
			}
		});

		// associate the gridview in the layout with an image adapter
		setUpGridView(STARTUP_DIRECTORY);
	}


	/*
	 * Hooks the GridView up with the Adapter. Also checks if the folder exists before attempting to load it.
	 */
	private void setUpGridView(String selectedFolder) {

		if (new File(selectedFolder).isDirectory()) {
			adapter = new ImageAdapter(this, selectedFolder);
		}
		else {
			Toast.makeText(MainActivity.this, selectedFolder + " is not a folder", Toast.LENGTH_LONG).show();
			adapter = new ImageAdapter(this, previousFolder);
		}

		gridview.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		// setting up the folder selection actionbar item
		MenuItem selectFolderItem = menu.findItem(R.id.menu_select_folder);
		SearchView searchView = (SearchView) selectFolderItem.getActionView();

		// always show the current directory
		searchView.setQuery(selectedFolder, false);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener( ) {
			@Override
			public boolean   onQueryTextChange( String newText ) {
				// not implemented
				return true;
			}

			// handles search queries that are submitted
			@Override
			public boolean onQueryTextSubmit(String query) {
				previousFolder = selectedFolder;

				// removes trailing spaces that appear using most predictive Android keyboards
				selectedFolder = query.trim();

				// forces a configuration change, so we can load from a different folder
				recreate();

				return true;
			}
		});

		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		// put values in bundle to save between configuration changes (e.g. language change or rotation)
		outState.putString(SELECTED_FOLDER_KEY, selectedFolder);
		outState.putString(PREVIOUS_FOLDER_KEY, previousFolder);
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		// retrieve values from bundle
		selectedFolder = savedInstanceState.getString(SELECTED_FOLDER_KEY);
		previousFolder = savedInstanceState.getString(PREVIOUS_FOLDER_KEY);


		// associate the gridview in the layout with an image adapter
		setUpGridView(selectedFolder);
	}

}
