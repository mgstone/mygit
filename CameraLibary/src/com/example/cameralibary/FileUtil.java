package com.example.cameralibary;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	
	public static final int MEDIA_TYPE_IMAGE = 1;

	public static Uri getOutputMediaFileUri(int type,Context mContext){
	      return Uri.fromFile(getOutputMediaFile(type,mContext));
	}

	@SuppressLint("ShowToast")
	public static File getOutputMediaFile(int type,Context mContext){
		File mediaStorageDir=null;
		if(Environment.getExternalStorageState()!=null){
		    mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "TestCameraFile");
		    if (! mediaStorageDir.exists()){
		        if (! mediaStorageDir.mkdirs()){
		            Log.d("MyCameraApp", "failed to create directory");
		            return null; 
		        }
		    }
		}else{
			 mediaStorageDir=new File(mContext.getCacheDir(),"TestCameraFile");
			 if (! mediaStorageDir.exists()){
			        if (! mediaStorageDir.mkdirs()){
			            Log.d("MyCameraApp", "failed to create directory");
			            return null;
			        }
			    }
			 Log.d("MyCameraApp","路径为"+mediaStorageDir);
		}
	    String timeStamp =  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
}
