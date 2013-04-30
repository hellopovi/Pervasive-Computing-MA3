package com.example.basicphotobrowser;

import java.lang.ref.WeakReference;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/*
 * Adapted from code found on: http://developer.android.com/training/displaying-bitmaps/process-bitmap.html
 * This is supposed to get around certain concurrency problems associated with GridViews,
 * when loading more bitmaps than can be shown on screen.
 * 
 * It also handles the showing of a placeholder bitmap before the real thing has been loaded.
 */
public class AsyncDrawable extends BitmapDrawable {
	
	private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
	
	String filePath;
	
	public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
		super(res, bitmap);
		
		bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
	}
	
	public BitmapWorkerTask getBitmapWorkerTask() {
		
		 return bitmapWorkerTaskReference.get();
	}
}
