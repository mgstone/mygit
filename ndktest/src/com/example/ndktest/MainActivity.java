    package com.example.ndktest;  
    import android.app.Activity;  
    import android.graphics.Bitmap;  
    import android.graphics.Bitmap.Config;  
    import android.graphics.drawable.BitmapDrawable;  
    import android.os.Bundle;  
    import android.view.View;  
    import android.widget.Button;  
    import android.widget.ImageView;  
    public class MainActivity extends Activity {  
        /** Called when the activity is first created. */  
        Button btnJAVA,btnNDK;  
        ImageView imgView;  
        @Override  
        public void onCreate(Bundle savedInstanceState) {  
            super.onCreate(savedInstanceState);  
            setContentView(R.layout.activity_main);  
            this.setTitle("使用NDK转换灰度图---hellogv");  
            btnJAVA=(Button)this.findViewById(R.id.btnJAVA);  
            btnJAVA.setOnClickListener(new ClickEvent());  
              
            btnNDK=(Button)this.findViewById(R.id.btnNDK);  
            btnNDK.setOnClickListener(new ClickEvent());  
            imgView=(ImageView)this.findViewById(R.id.ImageView01);  
        }  
        class ClickEvent implements View.OnClickListener{  
            @Override  
            public void onClick(View v) {  
                if(v==btnJAVA)  
                {  
                    long current=System.currentTimeMillis();  
                    Bitmap img=ConvertGrayImg(R.drawable.cat);  
                    long performance=System.currentTimeMillis()-current;  
                    //显示灰度图  
                    imgView.setImageBitmap(img);  
                    MainActivity.this.setTitle("w:"+String.valueOf(img.getWidth())+",h:"+String.valueOf(img.getHeight())  
                            +" JAVA耗时 "+String.valueOf(performance)+" 毫秒");  
                }  
                else if(v==btnNDK)  
                {  
                    long current=System.currentTimeMillis();  
                      
                    //先打开图像并读取像素  
                    Bitmap img1=((BitmapDrawable) getResources().getDrawable(R.drawable.cat)).getBitmap();  
                    int w=img1.getWidth(),h=img1.getHeight();  
                    int[] pix = new int[w * h];  
                    img1.getPixels(pix, 0, w, 0, 0, w, h);  
                    //通过ImgToGray.so把彩色像素转为灰度像素  
                    int[] resultInt=LibFuns.ImgToGray(pix, w, h);  
                    Bitmap resultImg=Bitmap.createBitmap(w, h, Config.RGB_565);  
                    resultImg.setPixels(resultInt, 0, w, 0, 0,w, h);  
                    long performance=System.currentTimeMillis()-current;  
                    //显示灰度图  
                    imgView.setImageBitmap(resultImg);  
                    MainActivity.this.setTitle("w:"+String.valueOf(img1.getWidth())+",h:"+String.valueOf(img1.getHeight())  
                            +" NDK耗时 "+String.valueOf(performance)+" 毫秒");  
                }  
            }  
        }  
          
        /** 
         * 把资源图片转为灰度图 
         * @param resID 资源ID 
         * @return 
         */  
        public Bitmap ConvertGrayImg(int resID)  
        {  
            Bitmap img1=((BitmapDrawable) getResources().getDrawable(resID)).getBitmap();  
              
            int w=img1.getWidth(),h=img1.getHeight();  
            int[] pix = new int[w * h];  
            img1.getPixels(pix, 0, w, 0, 0, w, h);  
              
            int alpha=0xFF<<24;  
            for (int i = 0; i < h; i++) {    
                for (int j = 0; j < w; j++) {    
                    // 获得像素的颜色    
                    int color = pix[w * i + j];    
                    int red = ((color & 0x00FF0000) >> 16);    
                    int green = ((color & 0x0000FF00) >> 8);    
                    int blue = color & 0x000000FF;    
                    color = (red + green + blue)/3;    
                    color = alpha | (color << 16) | (color << 8) | color;    
                    pix[w * i + j] = color;  
                }  
            }  
            Bitmap result=Bitmap.createBitmap(w, h, Config.RGB_565);  
            result.setPixels(pix, 0, w, 0, 0,w, h);  
            return result;  
        }  
    }  