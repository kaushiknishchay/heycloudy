package com.kaushik.heycloudy.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class PermissionUtils {


    static final int STORAGE_PERMISSION_INT = 191;

    public static int doPermissionCheck(Context mContext) {

        AppCompatActivity mActivity = (AppCompatActivity) mContext;

        int permissionCheck = ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission Granted");
            return 1;
        } else {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_INT);
                    } else {
                        mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_INT);
                    }
            }
        }
        return 0;
    }
}
