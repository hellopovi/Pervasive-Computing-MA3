package com.example.basicphotobrowser;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.content.Context;
import android.os.AsyncTask;

public class ImageSender extends AsyncTask<File, Void, Integer> {

	Context context;	
	private String ipaddress;
	private int port;

	public ImageSender(Context context, String ipaddress, int port) {
		this.context = context;
		this.ipaddress = ipaddress;
		this.port = port;
	}

	@Override
	protected Integer doInBackground(File... params) {

		File[] file = new File[params.length];

		for (int i=0;i<params.length;i++){
			file[i] = (File) params[i]; //gets an array with all the image-files.
		}
		for (int j=0; j<file.length;j++){
			try {
				sendFile(file[j]);
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1; // 1 means successfully sent.

	}

	public void sendFile(File f) throws IOException {

		Socket socket = new Socket(ipaddress, port);  
		OutputStream outputStream = socket.getOutputStream();
		byte [] buffer = new byte[(int)f.length()];
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(buffer,0,buffer.length);
		outputStream.write(buffer,0,buffer.length);
		outputStream.flush();
		socket.close();
		bis.close();

	}
}
