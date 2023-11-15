package com.example.rongx5.util;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;

public class ScreenUnit {
    private static PowerManager.WakeLock wl = null;
    public static void wark(Context context){
        // 获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            // ScreenOn是LogCat里用的Tag
        if(wl == null)
        {
            wl = pm.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "testDemo:ScreenOn");
        }

        boolean screenOn = pm.isInteractive();
        if(!screenOn)
        {
            wl.acquire(1000); // 点亮屏幕
        }

        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
        // 屏幕锁定
        keyguardLock.reenableKeyguard();
        keyguardLock.disableKeyguard(); // 解锁
    }
    public static void release(){
        if(wl != null)
        {
            wl.release();
        }
    }
}

