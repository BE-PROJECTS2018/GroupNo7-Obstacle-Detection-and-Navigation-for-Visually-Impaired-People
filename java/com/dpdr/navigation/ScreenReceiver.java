package com.dpdr.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Nisha on 27-Feb-18.
 */

public class ScreenReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            Intent intent1 = new Intent(context,MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}