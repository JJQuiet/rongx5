package com.example.rongx5.util.js;

import com.example.rongx5.MainActivity;
import com.example.rongx5.util.StorageConfig;
import com.example.rongx5.util.video.VideoCall;

import org.json.JSONException;
import org.json.JSONObject;


public class NativeUnit {
    public static void receive(String json){

        MainActivity.instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String type = jsonObject.get("type").toString();
                    if(type.equals("call"))
                    {
                        String isVideo = jsonObject.get("isVideo").toString();
                        String phone = jsonObject.get("phone").toString();
                        VideoCall._ins.outgoingCall(isVideo.equals("1"), phone);
                        return;
                    }
                    if(type.equals("saveConfig"))
                    {
                        String pbx = jsonObject.get("pbx").toString();
                        String fspbx = jsonObject.get("fspbx").toString();
                        String leftHand = jsonObject.get("leftHand").toString();
                        StorageConfig.saveConfig(pbx,fspbx,leftHand);
                        return;
                    }
                    if(type.equals("hangup"))
                    {
                        VideoCall._ins.hangUp();
                        return;
                    }
                    if(type.equals("readConfig"))
                    {
                        return;
                    }
                    if(type.equals("answer"))
                    {
                        VideoCall._ins.answer();
                        return;
                    }
                    if(type.equals("wark"))
                    {
                        return;
                    }
                    if(type.equals("close"))
                    {
                        return;
                    }
                    if(type.equals("setMute"))
                    {
                        return;
                    }
                    if(type.equals("showFullScreenVideo"))
                    {
                        VideoCall._ins.showFullScreen();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
