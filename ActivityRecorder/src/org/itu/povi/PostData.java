package org.itu.povi;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class PostData extends AsyncTask<Void, Void, Void> {
	private Context context;
	private String httpResponseStatus;

	public PostData(Context cxt) {
		context = cxt;
	}

	protected Void doInBackground(Void... params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://dataanalyzerma2.appspot.com/dataanalyzer");
			HttpResponse response = httpClient.execute(httpPost);
			Log.i("POVI", "SENDING MESSAGE");
			httpResponseStatus = response.getStatusLine().toString();

		} catch (ClientProtocolException e) {
			Log.i("POVI", e.toString());
		} catch (IOException e) {
			Log.i("POVI", e.toString());
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Toast.makeText(context, httpResponseStatus, Toast.LENGTH_LONG).show();
	}
}