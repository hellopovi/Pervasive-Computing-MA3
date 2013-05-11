package com.guseggert.sensorlogger;

import java.util.HashMap;
import java.util.Map;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Spinner;
import android.widget.TextView;

public class UIUpdater {

	private MainActivity mActivity;
	private SensorLogger mSensorLogger;
	private TextView mTextAccX;
	private TextView mTextAccY;
	private TextView mTextAccZ;
	private Button mButtonStart;
	private Chronometer mChronometer;
	
	public UIUpdater(final MainActivity act) {
		mActivity = act;
		mSensorLogger = mActivity.getSensorLogger();
		initUIObjs();
		initActivitySpinner();
		initStartButton();
		initChronometer();
	}
	
	private void initChronometer() {
		mChronometer = (Chronometer)mActivity.findViewById(R.id.chronometer1);
		
	}
	
	private void initUIObjs() {
		mTextAccX = (TextView)mActivity.findViewById(R.id.acc_x_value);
		mTextAccY = (TextView)mActivity.findViewById(R.id.acc_y_value);
		mTextAccZ = (TextView)mActivity.findViewById(R.id.acc_z_value);
	}
	
	private void initActivitySpinner() {
		Spinner spinner = (Spinner) mActivity.findViewById(R.id.spn_activity);
		ArrayAdapter<CharSequence> adapter = 
				ArrayAdapter.createFromResource(mActivity, R.array.activities_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				mSensorLogger.updateActivity(arg0.getSelectedItem().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		mSensorLogger.updateActivity(spinner.getSelectedItem().toString());
	}
		
	private void initStartButton() {
		mButtonStart = (Button)mActivity.findViewById(R.id.btn_start);
		mButtonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onStartButtonClick();
			}
		});
	}
	
	private void onStartButtonClick() {
		if (mButtonStart.getText().equals("Start")) {
			mButtonStart.setText("Stop");
			startChronometer();
			mSensorLogger.start();
		}
		else {
			mButtonStart.setText("Start");
			stopChronometer();
			mSensorLogger.stop();
		}
	}
	
	private void startChronometer() {
		mChronometer.setBase(SystemClock.elapsedRealtime());
		mChronometer.start();
	}
	
	private void stopChronometer() {
		mChronometer.stop();
		
	}
	
	public void updateValues(HashMap<SensorID, Float> data) {
		for (Map.Entry<SensorID, Float> entry : data.entrySet()) {
			switch (entry.getKey()) {
			case ACC_X:
				mTextAccX.setText(Float.toString(entry.getValue()));
				break;
			case ACC_Y:
				mTextAccY.setText(Float.toString(entry.getValue()));
				break;
			case ACC_Z:
				mTextAccZ.setText(Float.toString(entry.getValue()));
				break;
			default:
				Log.e("MainActivity", "MainActivity.updateValues() received an invalid sensor type");
				break;
			}
		}
	}


}
