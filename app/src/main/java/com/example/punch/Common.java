package com.example.punch;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import static com.example.punch.MainActivity.context;

/**
 * Java class for common functions
 */

@SuppressLint("SimpleDateFormat")
public class Common {

    public static boolean CheckConnection(Context context) {
        boolean check = false;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null) {
            if (wifi.isConnectedOrConnecting() == true) {
                check = true;
            }
        }
        else if(mobile!=null)
        {
            if(mobile.isConnectedOrConnecting() == true)
            {
                check=true;
            }
        }
        return check;
    }

    public static String GetMACAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        return address;
    }

    public static String GetIPAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        int address = info.getIpAddress();
        return String.valueOf(address);
    }

    public static String GetCurrentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date dateobj = new Date();
        String date = df.format(dateobj);
        return date;
    }

    public static File getDir() {
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "Punch");
    }

    public static int findFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    public static void configureSyncJob() {
        new JobRequest.Builder(SyncRemoteConfig.TAG)
                .setPeriodic(21600000)
                .setUpdateCurrent(true)
                .setPersisted(true)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .build()
                .schedule();
    }

    public static class SyncRemoteConfig extends Job {

        public static final String TAG = "sync_job";

        @NonNull
        @Override
        protected Result onRunJob(Params params) {

            GetUsers users = new GetUsers(PunchApp.getAppContext(), false);
            users.start();
            Toast.makeText(PunchApp.getAppContext(), "You are Updating your User data.", Toast.LENGTH_LONG).show();

            return Result.SUCCESS;
        }
    }

    /*public void scheduleAlarms(Context context) {
    	Calendar calendar = Calendar.getInstance();

    	calendar.set(Calendar.HOUR_OF_DAY, 5); // For 1 PM or 2 PM
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	PendingIntent pi = PendingIntent.getService(context, 0,new Intent(context, GetUsers.class),PendingIntent.FLAG_UPDATE_CURRENT);
    	AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    	am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi);
    }*/


}
