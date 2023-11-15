package com.example.rongx5.util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class TelpoReceiver extends BroadcastReceiver {

    private final String TAG = "TelpoReceiver";
    private Context mContext;

    private final String ACTION_HANDLE_PLUG = "android.intent.action.HANDLE_PLUG";
    private final String ACTION_HANDSFREE_KEY = "android.intent.action.HANDSFREE_KEY";
    private final String TELPO_HANDLE_PLUG = "telpo.intent.action.HANDLE_PLUG";
    private final String TELPO_HANDSFREE_KEY = "telpo.intent.action.HANDSFREE_KEY";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        handleIntent(intent,context);
    }


    private void handleIntent(Intent intent,Context context){
        String action = intent.getAction();
        Log.i(this.TAG,action);
        if(action.equals(ACTION_HANDLE_PLUG) ){
            int state_plug = intent.getIntExtra("state", 0);
            handleHandlePlug(state_plug,context);
            return;
        }
        if(action.equals(ACTION_HANDSFREE_KEY)){
            int state_hf = intent.getIntExtra("action", 0);
            handleHandsfree(state_hf);
            return;
        }
        Log.i(TAG, "unknown action");

    }


    private void handleHandlePlug(int state,Context context){
        if(state == 1)
        {
            Log.i(TAG, "手柄拿起");
//            Js.call("HandUpOperation");
            return;
        }
        Log.i(TAG, "手柄放下");
//        Js.call("HandDownOperation");
        return;
    }

    private void handleHandsfree(int state){
        Log.i(TAG, "hansfree key state = "+state);
//        Js.call("HandsFreeOperation");
//        //点亮屏幕
//        Native.wark();
//        if(state == 1)
//        {
//            Log.i(TAG, "免提打开");
//            return;
//        }
//        Log.i(TAG, "免提关闭");
        return;
    }
}
