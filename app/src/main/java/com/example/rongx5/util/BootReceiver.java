package com.example.rongx5.util;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.rongx5.MainActivity;

import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    static final private String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "自启动了 ！！！！！");
//            if(this.isAppAlive(context,"org.cocos2d.demo"))
//            {
//                log.info("杀掉以前的进程重启 ！！！！！");
//                this.restartApplication(context);
//                return;
//            }
            Intent newIntent = new Intent(context, MainActivity.class);  // 要启动的Activity
            //1.如果自启动APP，参数为需要自动启动的应用包名
            //Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            //这句话必须加上才能开机自动运行app的界面
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //2.如果自启动Activity
            context.startActivity(newIntent);
            //3.如果自启动服务
            //context.startService(newIntent);

        }
    }
//    private void restartApplication(Context context) {
//
//        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(getPackageName());
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);
//        //杀掉以前进程
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }
    /**
     * 判断应用是否已经启动
     *
     * @param context   上下文对象
     * @param packageName 要判断应用的包名
     * @return boolean
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
