package com.example.rongx5.util;

import android.view.KeyEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.example.rongx5.MainActivity;


public class JKeyBoard {
    class KBReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            receive(context, intent);
        }
    }

    // 1. 按键键值
    private static final int KEYCODE_PTT = 284;
    private static final int KEYCODE_HS_PTT = 285;
    private static final int KEYCODE_SOS = 286;
    private static final int KEYCODE_VID_RECORD = 287;
    private static final int KEYCODE_AUD_RECORD = 288;

    private IntentFilter mKBIntentFilter = null;
    private KBReceiver mKBReceiver = null;

    // 2. 按下按键，系统会发送如下action
    public static final String ACTION_HANDLE_PLUG = "android.intent.action.HANDLE_PLUG";

    // 3. 长按键，需要根据 ACTION_DOWN 和 ACTION_UP，定时判断，应用自行定义长按键时间

    //evt.getAction() == KeyEvent.ACTION_UP
    //evt.getAction() == KeyEvent.ACTION_DOWN

    public static final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";
    public static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";

    static final private String TAG = "JKeyBoard";
    static private JKeyBoard mInstance = new JKeyBoard();
    private Context mContext = null;

	/*
	//private KeyguardManager  km;
	//private KeyguardLock kl;
	private PowerManager pm;
	private PowerManager.WakeLock wakeLock=null;

	private void acquireWakeLock() {
		if ((null == wakeLock)&&(mContext!=null)) {
		pm = (PowerManager)mContext.getSystemService(mContext.POWER_SERVICE);
		wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, getClass().getCanonicalName());

		if (null != wakeLock) {
			Log.i(TAG, "call acquireWakeLock");
			wakeLock.acquire();
			}
		}
	}
	*/

    private JKeyBoard() {
    }

    static public JKeyBoard instance() {
        return mInstance;
    }

    public void setContext(Context context) {
        Log.d(TAG, "context: " + context.toString());
        mContext = context;

        //acquireWakeLock();

        mKBReceiver = new KBReceiver();
        mKBIntentFilter = new IntentFilter();//(ACTION_HANDLE_PLUG);
        mKBIntentFilter.addAction(ACTION_HANDLE_PLUG);
        //mKBIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        //mKBIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mKBIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mKBIntentFilter.addAction(ACTION_SCREEN_OFF);
        mKBIntentFilter.addAction(ACTION_SCREEN_ON);
        //mKBIntentFilter.setPriority(10000);
        start();
    }

    public void start() {
        mContext.registerReceiver(mKBReceiver, mKBIntentFilter);
        //System.out.printf("+++++++++++++++__________+++++++++++++++++11111...start....\n");
    }

    public void stop() {
        mContext.unregisterReceiver(mKBReceiver);
    }

    private void receive(Context context, Intent intent) {
        String action = intent.getAction();
        //System.out.printf("+++++++++++++++__________+++++++++++++++++22222...%s\n",action);
        if (action.equals(ACTION_HANDLE_PLUG)) {
            if (intent.hasExtra("state") && MainActivity.instance != null) {
                int i_state = intent.getIntExtra("state", 0);//0挂机，1摘机
                if (i_state == 1) {
                    MainActivity.instance.handevent(i_state);
//                    Js.call("HandUpOperation");
                } else if (i_state == 0) {
                    MainActivity.instance.handevent(i_state);
//                    Js.call("HandDownOperation")
                }
//                notifyKeyEvent(0,i_state);
            }
        }
//        else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
//        {
//            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//            String connType = "";
//            int netcode=-255;
//            int netstate=-1;
//            if (info != null) {
//                //如果当前的网络连接成功并且网络连接可用
//                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
//                    if (info.getType() == ConnectivityManager.TYPE_WIFI){
//                        connType="WIFI";
//                        netstate=1;
//                    }else if (info.getType() == ConnectivityManager.TYPE_MOBILE){
//                        connType="4G";
//                        netstate=4;
//                    }
//                }
//                else {
//                    connType="DISC";
//                    netstate=0;
//                }
//            }
//            notifyKeyEvent(netcode,netstate);
//            //System.out.printf("+++++++++++++++__________+++++++++++++++++11111.......%s\n",connType);
//        }
		/*
		if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION))// 监听wifi的打开与关闭，与wifi的连接无关
		{
			System.out.printf("+++++++++++++++__________+++++++++++++++++11111...WifiManager.WIFI_STATE_CHANGED_ACTION....\n");
		}
		if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))// 监听wifi的连接状态即是否连上了一个有效无线路由
		{
			System.out.printf("+++++++++++++++__________+++++++++++++++++11111...WifiManager.NETWORK_STATE_CHANGED_ACTION....\n");
		}*/

//        else if (action.equals(ACTION_SCREEN_OFF))
//        {
//            int scrcode=-1024;
//            int scrstate=0;
//            notifyKeyEvent(scrcode,scrstate);
//            //System.out.printf("+++++++++++++++__________+++++++++++++++++11111...ACTION_SCREEN_OFF....\n");
//        }
//        else if (action.equals(ACTION_SCREEN_ON))
//        {
//            int scrcode=-1024;
//            int scrstate=1;
//            notifyKeyEvent(scrcode,scrstate);
//            //System.out.printf("+++++++++++++++__________+++++++++++++++++11111...ACTION_SCREEN_OFF....\n");
//        }
    }

    //    public static native void notifyKeyEvent(int keycode,int up_down);
    public int addTest(int value0, int value1) {
        Log.d(TAG, "I'm a test.");
        return value0 + value1;
    }
}

