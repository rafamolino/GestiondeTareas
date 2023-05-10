package com.example.gestiondetareas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyFirebaseMessagingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MyFirebaseMessagingService.class);
        serviceIntent.putExtras(intent.getExtras());
        context.startService(serviceIntent);
    }
}
