package com.trinerva.icrm.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class StorageUtility {
	public static String TAG = "StorageUtility";
	public File fCacheDir;
	
	public StorageUtility(Context context, String strCacheFolder) {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			this.fCacheDir = new File(android.os.Environment.getExternalStorageDirectory(), strCacheFolder);
		} else {
			this.fCacheDir = context.getCacheDir();		
		}

		if (!this.fCacheDir.exists()) {
			this.fCacheDir.mkdirs();
		}
		DeviceUtility.log(TAG, "Cache Path: " + this.fCacheDir.getAbsolutePath());
	}
	
	public String doGenerateUUID() {
		String strUUID = UUID.randomUUID().toString();
		DeviceUtility.log(TAG, "strUUID: " + strUUID);
		return strUUID;
	}
	
	public String doStoreImage(Bitmap bitmap) {
		String strUUID = doGenerateUUID();		
		File fStoreFile = new File(this.fCacheDir, strUUID);
		if (fStoreFile.exists()) {
			//regen
			strUUID = doGenerateUUID();
			fStoreFile = new File(this.fCacheDir, strUUID);
		}
		doStoreImage(bitmap, strUUID);
		return strUUID;
	}
	
	public boolean doStoreImage(Bitmap bitmap, String strUUID) {
		boolean bStored = true;
		OutputStream mOutStream = null;
		File fStoreFile = new File(this.fCacheDir, strUUID);
		try {
			mOutStream = new FileOutputStream(fStoreFile);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, mOutStream);
			mOutStream.flush();
			mOutStream.close();
		} catch (Exception e) {
			bStored = false;
			e.printStackTrace();
		}
		return bStored;
	}
	
	public boolean doDeleteImage(String strFilename) {
		boolean bDeleted = true;
		try {
			File fDeleteFile = new File(this.fCacheDir, strFilename);
			if (fDeleteFile.exists()) {
				fDeleteFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			bDeleted = false;
		}
		return bDeleted;
	}
	
	public Bitmap doReadImage(String strFilename) {
		File fImage = new File(this.fCacheDir, strFilename);
		//Bitmap bitmap = BitmapFactory.decodeFile(fImage.getAbsolutePath());
		try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(fImage), null, o);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE=100;

	        //Find the correct scale value. It should be the power of 2.
	        int width_tmp = o.outWidth, height_tmp = o.outHeight;
	        int scale = 1;
	        while (true) {
	            if ((width_tmp/2) < REQUIRED_SIZE || (height_tmp/2) < REQUIRED_SIZE) {
	                break;
	            }
	            width_tmp /= 2;
	            height_tmp /= 2;
	            scale *= 2;
	        }

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = scale;
	        return BitmapFactory.decodeStream(new FileInputStream(fImage), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}
}
