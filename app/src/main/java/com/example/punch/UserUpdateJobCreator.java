package com.example.punch;

import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Created by deeppandya on 2016-11-10.
 */

public class UserUpdateJobCreator implements JobCreator {
    private static final String TAG = "HexlockJobCreator";

    @Override
    public Job create(String tag) {
        switch (tag) {
            case Common.SyncRemoteConfig.TAG:
                return new Common.SyncRemoteConfig();
            default:
                Log.w(TAG, "Cannot find job for tag " + tag);
                return null;
        }
    }
}