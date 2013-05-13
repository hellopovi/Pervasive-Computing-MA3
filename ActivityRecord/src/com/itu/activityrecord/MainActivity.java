package com.itu.activityrecord;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity {
	
	public EditText filename;
	public static Chronometer c;
	static TextView xView;
	static TextView yView;
	static TextView zView;
	static ToggleButton tButton;
	
	public SensorLogger logger;
	
	public void toggleActivityButton(View view) {
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			startChronometer();
			logger.start(filename.getText().toString());
		} else {
			logger.stop();
			stopChronometer();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tButton = (ToggleButton)findViewById(R.id.toggleButton1);
		tButton.setEnabled(false);
		filename = (EditText)findViewById(R.id.editText1);
		logger = new SensorLogger(getBaseContext());
		filename.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				 if (arg0 == null || arg0.length() == 0) {
			           tButton.setEnabled(false);
			        }
			        else {
			        	tButton.setEnabled(true);
			        }				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
		});
		xView = (TextView)findViewById(R.id.textView2);
		yView = (TextView)findViewById(R.id.textView3);
		zView = (TextView)findViewById(R.id.textView4);
		c = (Chronometer)findViewById(R.id.chronometer1);
	}

    public void startChronometer() {
    	filename.setEnabled(false);
    	c.setBase(SystemClock.elapsedRealtime());
        c.start();
    }

    public void stopChronometer() {
    	filename.setEnabled(true);
        c.stop();
        updateDetails(true, new SensorData());
    }
	
	/**
	 * This function will update the information fields
	 * @param clean - boolean - define to clear data or update
	 * @param data - SensorData - object of the sensordata
	 */
	static void updateDetails(boolean clean, SensorData data) {
		if (clean == true) {
			c.setBase(SystemClock.elapsedRealtime());
			xView.setText("X: ");
			yView.setText("Y: ");
			zView.setText("Z: ");
		} else {
			xView.setText("X: " + Float.toString(data.x));
			yView.setText("Y: " + Float.toString(data.y));
			zView.setText("Z: " + Float.toString(data.z));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void sendData(String filename, String x, String y, String z, String timestamp) {
		
		class SendDataTask extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... params) {
				
				String _filename = params[0];
				String _x = params[1];
				String _y = params[2];
				String _z = params[3];
				String _time = params[4];
				
				HttpClient httpClient = new DefaultHttpClient(); 
				HttpPost httpPost = new HttpPost("http://activityrecorder.appspot.com/dataanalyzer");
				
				BasicNameValuePair filenamePair = new BasicNameValuePair("filename", _filename);
	            BasicNameValuePair xPair = new BasicNameValuePair("x", _x);
	            BasicNameValuePair yPair = new BasicNameValuePair("y", _y);
	            BasicNameValuePair zPair = new BasicNameValuePair("z", _z);
	            BasicNameValuePair timePair = new BasicNameValuePair("x", _time);
	            
	            // We add the content that we want to pass with the POST request to as name-value pairs
	            //Now we put those sending details to an ArrayList with type safe of NameValuePair
	            List<NameValuePair> listPair = new ArrayList<NameValuePair>();
	            listPair.add(filenamePair);
	            listPair.add(xPair);
	            listPair.add(yPair);
	            listPair.add(zPair);
	            listPair.add(timePair);
	            try {
	                // UrlEncodedFormEntity is an entity composed of a list of url-encoded pairs. 
	                //This is typically useful while sending an HTTP POST request. 
	                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(listPair);

	                // setEntity() hands the entity (here it is urlEncodedFormEntity) to the request.
	                httpPost.setEntity(urlEncodedFormEntity);

	                try {
	                    // HttpResponse is an interface just like HttpPost.
	                    //Therefore we can't initialize them
	                    HttpResponse httpResponse = httpClient.execute(httpPost);

	                } catch (ClientProtocolException cpe) {
	                    System.out.println("First Exception caz of HttpResponese :" + cpe);
	                    cpe.printStackTrace();
	                } catch (IOException ioe) {
	                    System.out.println("Second Exception caz of HttpResponse :" + ioe);
	                    ioe.printStackTrace();
	                }

	            } catch (UnsupportedEncodingException uee) {
	                System.out.println("An Exception given because of UrlEncodedFormEntity argument :" + uee);
	                uee.printStackTrace();
	            }
				
				return null;
			}
			
		}
		
		SendDataTask task = new SendDataTask();
		task.execute(filename, x, y, z, timestamp);
		
	}

}
