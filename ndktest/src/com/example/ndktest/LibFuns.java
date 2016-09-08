    package com.example.ndktest;  
    public class LibFuns {  
        static {  
            System.loadLibrary("ImgToGray");  
        }  
       /** 
        * @param width the current view width 
        * @param height the current view height 
        */  
          
        public static native int[] ImgToGray(int[] buf, int w, int h);  
    }  