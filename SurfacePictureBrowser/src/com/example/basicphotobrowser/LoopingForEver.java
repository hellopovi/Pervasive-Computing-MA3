package com.example.basicphotobrowser;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class LoopingForEver extends AsyncTask<Integer, Void, Void> {

	final String STARTUP_DIRECTORY = Environment.getExternalStorageDirectory() + "/Pictures/Pictures/";
	Context context;
	private int port;

	public LoopingForEver(Context context, int port) {
		this.context = context;
		this.port = port;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		// TODO Auto-generated method stub

		try {
			openConnectionForReceive();
			Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(i);

		} catch (IOException e) {

			Log.i("Error", "openConnectionForReceive() method");
			e.printStackTrace();
		}

		return null;
	}

	private void openConnectionForReceive() throws IOException {

		// NOTE: this will override any
		// other file with the name image1.jpg
		// String outputFilename = "image1.jpg"; // We need to make this
		// "dynamic" like the old code, for receival of more files

		ServerSocket ServerSocket = new ServerSocket(port);
		Socket currentInSocket = ServerSocket.accept();
		DataOutputStream dataStream = new DataOutputStream(currentInSocket.getOutputStream());
		InputStream inputStream = currentInSocket.getInputStream();

		int bufferSize = currentInSocket.getReceiveBufferSize();

		FileOutputStream fos = new FileOutputStream(STARTUP_DIRECTORY + getNextFileName()); // FileOutputStream(STARTUP_DIRECTORY+"Photo"+
																							// getNextFileName()
																							// +".jpg");
		BufferedOutputStream bos = new BufferedOutputStream(fos);

		byte[] bytes = new byte[bufferSize];
		int count;

		while ((count = inputStream.read(bytes)) != -1) {

			bos.write(bytes, 0, count);

		}
		bos.close();
		currentInSocket.close();
		ServerSocket.close();
		dataStream.close();
	}

	private String getNextFileName() {
		File sd = new File(STARTUP_DIRECTORY);
		File[] sdDirList = null;
		int newestFileName = 0;
		int up = -1;
		boolean upFound = false;
		if (sd.exists() && sd.isDirectory()) {
			sdDirList = sd.listFiles();
			for (File f : sdDirList) {
				try {
					up = Integer.parseInt(f.getName().substring(0, f.getName().length() - 4));
					if (up == 0)
						upFound = true;
				} catch (Exception e) {
					Log.i("namer", "non-jpg file found.");
				}
				if (newestFileName < up)
					newestFileName = up;
				Log.i("namer", "name changed!");
			}
		}
		newestFileName++;
		String name = String.valueOf(newestFileName);

		return name + ".jpg";
	}
}