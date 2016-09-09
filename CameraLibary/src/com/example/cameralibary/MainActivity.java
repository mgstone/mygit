package com.example.cameralibary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private ImageView image;
	private Button btn1,btn2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image=(ImageView)findViewById(R.id.image);
		btn1=(Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(CameraCheck.CheckCamera(MainActivity.this)){
					Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
				}
			}
			
		});
		btn2=(Button)findViewById(R.id.btn2);
		btn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,CameraActivity.class);
				startActivity(intent);
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try { 
            if (requestCode != CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE ) { 
                return; 
            } 
            super.onActivityResult(requestCode, resultCode, data); 
            Bundle extras = data.getExtras(); 
            Bitmap b = (Bitmap) extras.get("data"); 
            image.setImageBitmap(b);
            
        } catch (Exception e) { 
            // TODO: handle exception 
            System.out.println(e.getMessage()); 
        } 
	}
}
