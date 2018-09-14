package com.example.angel.downloader;

import android.os.Environment;

public class CheckForSDCard {

    public static boolean isSDCardPresent(){
        if(Environment.getExternalStorageDirectory().equals(
                Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }
}


//http://www.androiddeft.com/2018/03/18/android-download-file-from-url-with-progress-bar/