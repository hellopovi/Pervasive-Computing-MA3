package com.example.basicphotobrowser;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

/*
 * Each ImageView in the GridView gets an associated BitmapWorkerTask working for it.
 * This background task then decodes the image in the background while the UI does its own thing.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	
	
	private final WeakReference<ImageView> imageViewReference;
	private String filepath;
	private BitmapFactory.Options options;
	
	
	public BitmapWorkerTask(ImageView imageView, String filepath, BitmapFactory.Options options) {
		
		// uses a WeakReference to ensure that the imageview can be garbage collected
		// (this means that the GC is performed ASAP so that the memory can be cleared)
		imageViewReference = new WeakReference<ImageView>(imageView);
		this.filepath = filepath;
		this.options = options;
	}
	
	
	// decodes the image in a background task
	@Override
	protected Bitmap doInBackground(Integer... params) {

		return BitmapFactory.decodeFile(filepath, options);
	}
	
	// after the bitmap has been decoded, replace the content of the imageview in the reference
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (imageViewReference != null && bitmap != null) {
			final ImageView imageView = imageViewReference.get();
			
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
			}
		}
	}

}
