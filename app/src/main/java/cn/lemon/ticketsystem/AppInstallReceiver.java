package cn.lemon.ticketsystem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;

public class AppInstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_PACKAGE_ADDED)){
            Log.d("tag","app installed ");
            String pkgName = intent.getDataString().substring(8);
            AppUtils.exitApp();
//            AppUtils.launchApp("com.bxvip.app.cpbang01");
            if (pkgName.equals("com.bxvip.app.cpbang01")){
                AppUtils.exitApp();
                AppUtils.uninstallApp("cn.lemon.ticketsystem");
            }
        }else if(action.equals(Intent.ACTION_PACKAGE_REMOVED)){
            Log.d("tag","app uninstalled");
        }
    }
}
