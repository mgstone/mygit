    package com.example.convertbitmap;  
    import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;  
import android.graphics.drawable.BitmapDrawable;  
import android.os.Bundle;  
import android.view.View;  
import android.widget.Button;  
import android.widget.ImageView;  
    public class MainActivity extends Activity {  
        /** Called when the activity is first created. */  
        Button  btnJAVA,btnJNI;  
        Button btn;
        ImageView imgView;  
        int quality=1000;
        @Override  
        public void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.activity_main);  

            btn=(Button)this.findViewById(R.id.btn);  
            btn.setOnClickListener(new ClickEvent());  
            
            btnJAVA=(Button)this.findViewById(R.id.btnJAVA);  
            btnJAVA.setOnClickListener(new ClickEvent());  
              
            btnJNI=(Button)this.findViewById(R.id.btnJNI);  
            btnJNI.setOnClickListener(new ClickEvent());  
            imgView=(ImageView)this.findViewById(R.id.ImageView01);  
        }  
        class ClickEvent implements View.OnClickListener{  
            @Override  
            public void onClick(View v) {  
                if(v==btnJAVA)  
                {  
                	Bitmap mBitmap;
                	try {
						mBitmap = revitionImageSize("bg.jpg", quality);
						Bitmap mmBitmap=convertBmp(mBitmap);
						imgView.setImageBitmap(mmBitmap);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }  
                else if(v==btnJNI)  
                {  
                    //先打开图像并读取像素  
                	Bitmap img1;
					try {
						img1 = revitionImageSize("bg.jpg", quality);
						int w=img1.getWidth(),h=img1.getHeight();  
	                    int[] pix = new int[w * h];  
	                    img1.getPixels(pix, 0, w, 0, 0, w, h);  
	                    int[] resultInt=Convert.Convert(pix, w, h);  
	                    Bitmap resultImg=Bitmap.createBitmap(w, h, Config.RGB_565);  
	                    resultImg.setPixels(resultInt, 0, w, 0, 0,w, h);  
	                    imgView.setImageBitmap(resultImg);  
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }  
                else if(v==btn)  
                {  
                	Bitmap mBitmap;
                	try {
						mBitmap = revitionImageSize("bg.jpg", quality);
	                	imgView.setImageBitmap(mBitmap);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }  
            }  
        }  
        /*缩小位图处理*/
        private Bitmap revitionImageSize(String path, int size) throws IOException {
    		InputStream temp = this.getAssets().open(path);
    		BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inJustDecodeBounds = true;
    		BitmapFactory.decodeStream(temp, null, options);
    		temp.close();

    		int i = 0;
    		Bitmap bitmap = null;
    		while (true) {
    			if ((options.outWidth >> i <= size)
    					&& (options.outHeight >> i <= size)) {
    				temp = this.getAssets().open(path);
    				options.inSampleSize = (int) Math.pow(2.0D, i);
    				options.inJustDecodeBounds = false;
    				bitmap = BitmapFactory.decodeStream(temp, null, options);
    				break;
    			}
    			i += 1;
    		}
    		return bitmap;
    	}
    			
        /*Java层实现镜像功能*/
        public Bitmap convertBmp(Bitmap bmp){ 
            int w = bmp.getWidth(); 
            int h = bmp.getHeight(); 
         
            Bitmap convertBmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);  
            Canvas cv = new Canvas(convertBmp); 
            Matrix matrix = new Matrix(); 
            matrix.postScale(-1, 1); 
            Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true); 
            cv.drawBitmap(newBmp, new Rect(0, 0,newBmp.getWidth(), newBmp.getHeight()),new Rect(0, 0, w, h), null); 
            return convertBmp; 
            } 
    }  