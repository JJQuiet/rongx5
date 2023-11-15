package com.example.rongx5.util.js;


import com.example.rongx5.util.GlobalConfig;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;

public class AppConfig {
    public static String ip = "192.168.0.47";
    public static String fsip="192.168.0.47";
    public static String pbx = "192.168.0.150";
    public static String fspbx = "192.168.0.19";

    public static  String leftHand = "1010";
    public static String handPwd = "54321321";

    public static void init(){
        String str = GlobalConfig.ipconfig;
        String[] arr = str.split("\r\n");
        Map<String,String> map = new HashMap<String,String>();
        for (String content:
                arr) {
            content = content.replaceAll(" ","");
            if(StrUtil.isEmpty(content) == false)
            {
                String[] conArr = content.split("=");
                if(conArr.length ==2)
                {
                    map.put(conArr[0],conArr[1]);
                }
            }
        }
        AppConfig.pbx = map.get("pbx");
        AppConfig.leftHand = map.get("leftHand");
        AppConfig.handPwd = map.get("handPwd");
        AppConfig.ip = map.get("ip");
        AppConfig.fsip = map.get("fsip");
        AppConfig.fspbx = map.get("fspbx");
    }
}

