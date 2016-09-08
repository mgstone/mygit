    package com.example.convertbitmap;  
    public class Convert {  
        static {  
            System.loadLibrary("Convert");  
        }  
       /** 
        * @param width the current view width 
        * @param height the current view height 
        */  
          
        public static native int[] Convert(int[] buf, int w, int h);  
    }  