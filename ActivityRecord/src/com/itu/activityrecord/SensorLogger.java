package com.itu.activityrecord;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.widget.TextView;

public class SensorLogger implements SensorEventListener {

	private SensorManager _sensorManager;
	private Sensor _sensor;
	private ArrayList<SensorData> _data;
	private String _filename;
	private Context context;

	public SensorLogger(Context c) {
		this.context = c;
	}
	
	/**
	 * This function will start the logging of the data
	 * 
	 * @param fn
	 *            String - The file name to which store the data
	 */
	public void start(String filename) {
		this._filename = filename;
		this._data = new ArrayList<SensorData>();
		this._sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		this._sensor = this._sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this._sensorManager.registerListener(this, this._sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public void stop() {
		this._sensorManager.unregisterListener(this);
		saveSensorData(this._data);
	}
	
	/**
	 * This function will save logging data and after will send it to the app engine
	 * @param data
	 */
	private void saveSensorData(ArrayList<SensorData> data) {
		FileOutputStream outputStream;

		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'_HH-mm");
			String now = formatter.format(new Date());
			String csvHeader = "x,y,z,time\n";
			String fileNameInput = now + "_" + this._filename + ".csv";
			outputStream = context.openFileOutput(fileNameInput, Context.MODE_PRIVATE);

			outputStream.write(csvHeader.getBytes());			
			ArrayList<SensorData> cleanData = new ArrayList<SensorData>();
			cleanData = cleandata(data);
			
			for (SensorData item : data) {
				// write every line of sensor data separately to file
				outputStream.write((item.x + "," + item.y + "," + item.z + ","
						+ item.time + "\n").getBytes());
			}
			outputStream.close();
			sendFileToServer(fileNameInput);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// Save the data to arraylist
			SensorData d = new SensorData(event.timestamp, event.values[0],
					event.values[1], event.values[2]);
			this._data.add(d);
			// TODO:Update the on screen details
			MainActivity.updateDetails(false, d);
		}

	}

	public ArrayList<SensorData> cleandata(ArrayList<SensorData> data) {
		// Remove 5 seconds from the beginning, remove 5 second in the end
		// It will give us more correct data, while we putting mobile in the
		// pocket
		// and taking it of the pocket
		int length = data.size() - 10;
		ArrayList<SensorData> list = new ArrayList<SensorData>(length);
		// Cut off first 5 seconds
		for (int i = 5; i < length; i++) {
			// Define new values
			float newx = 0;
			float newy = 0;
			float newz = 0;
			// Sum 5 next elements and later on make an average of it due to
			// cleaner data
			for (int j = 0; j < 5; j++) {
				newx += data.get(i + j).x;
				newy += data.get(i + j).y;
				newz += data.get(i + j).z;
			}
			// Add the values to the list
			list.add(new SensorData((data.get(i).time), (newx / 5), (newy / 5),
					(newz / 5)));

		}
		return list;
	}
	
	private void sendFileToServer(String fileName) {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		DataInputStream inputStream = null;

		String pathToOurFile = fileName;
		String urlServer = "http://192.168.1.1/handle_upload.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary =  "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;

		try
		{
		FileInputStream fileInputStream = context.openFileInput(pathToOurFile);

		URL url = new URL(urlServer);
		connection = (HttpURLConnection) url.openConnection();

		// Allow Inputs & Outputs
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);

		// Enable POST method
		connection.setRequestMethod("POST");

		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

		outputStream = new DataOutputStream( connection.getOutputStream() );
		outputStream.writeBytes("Content-Disposition: form-data; name=\"recordsfile\";filename=\"" + pathToOurFile +"\"" + lineEnd);
		outputStream.writeBytes(lineEnd);

		bytesAvailable = fileInputStream.available();
		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		buffer = new byte[bufferSize];

		// Read file
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);

		while (bytesRead > 0)
		{
		outputStream.write(buffer, 0, bufferSize);
		bytesAvailable = fileInputStream.available();
		bufferSize = Math.min(bytesAvailable, maxBufferSize);
		bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		}

		outputStream.writeBytes(lineEnd);
		outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

		// Responses from the server (code and message)
		//serverResponseCode = connection.getResponseCode();
		//serverResponseMessage = connection.getResponseMessage();

		fileInputStream.close();
		outputStream.flush();
		outputStream.close();
		}
		catch (Exception ex)
		{
		//Exception handling
		}
	}

}
