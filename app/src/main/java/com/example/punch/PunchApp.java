package com.example.punch;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.evernote.android.job.JobManager;

public class PunchApp extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext=this;

        JobManager.create(this).addJobCreator(new UserUpdateJobCreator());

        Common.configureSyncJob();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
