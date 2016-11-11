package com.example.punch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    /**
     * Java class to set alarm to update user dataset
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        GetUsers users = new GetUsers(context, false);
        users.start();
        Toast.makeText(context, "You are Updating your User data.", Toast.LENGTH_LONG).show();
    }

}
      
