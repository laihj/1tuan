package net.laihj.ytuan;         ;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;



import net.laihj.ytuan.ytuanApplication;
import net.laihj.ytuan.UpdateService;


public class AlarmSetBoardcaseReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
	public void onReceive(Context context, Intent intent) {
 
	if (intent.getAction().equals(ACTION)){
	     context.startService(new Intent(context, UpdateService.class)); 
	}
    }
}