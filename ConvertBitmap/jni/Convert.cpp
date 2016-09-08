    #include <jni.h>
    #include <stdio.h>
    #include <stdlib.h>
   using namespace std;
    extern "C" {
    JNIEXPORT jintArray JNICALL Java_com_example_convertbitmap_Convert_Convert(
            JNIEnv* env, jobject obj, jintArray buf, int w, int h);
    }
    ;
    JNIEXPORT jintArray JNICALL Java_com_example_convertbitmap_Convert_Convert(
            JNIEnv* env, jobject obj, jintArray buf, int w, int h) {
        jint *cbuf;
        cbuf = env->GetIntArrayElements(buf, false);
        if (cbuf == NULL) {
            return 0; /* exception occurred */
        }
        int alpha = 0xFF << 24;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w/2; j++) {
               //像素交换
                int tmp=cbuf[i*w+j];
                cbuf[i*w+j]=cbuf[(i+1)*w-j-1];
                cbuf[(i+1)*w-j-1]=tmp;
            }
        }

        int size=w * h;
        jintArray result = env->NewIntArray(size);
        env->SetIntArrayRegion(result, 0, size, cbuf);
        env->ReleaseIntArrayElements(buf, cbuf, 0);
        return result;
    }
