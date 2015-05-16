package edu.washington.gyb2015.awty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by iguest on 5/14/15.
 */
public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(final Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Bundle mBundle = intent.getExtras();
        String phone = mBundle.getString("phone", "mBundle fail");
        String message = mBundle.getString("message", "MBundle fail");
        Toast.makeText(context, phone + ": " + message, Toast.LENGTH_SHORT).show();
    }
}