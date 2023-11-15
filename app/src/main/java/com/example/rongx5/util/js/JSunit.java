package com.example.rongx5.util.js;

import com.example.rongx5.MainActivity;
import com.tencent.smtt.sdk.WebView;

public class JSunit {


    public static void IncomingReceived(String isVideo,String fromAddress){
        WebView webView = MainActivity.instance.getWebView();
        if(webView == null)return;
        webView.loadUrl("javascript:AndroidIncomingReceived("+isVideo+","+fromAddress+")");
    }
    public static void StreamsRunning(){
        WebView webView = MainActivity.instance.getWebView();
        if(webView == null)return;
        webView.loadUrl("javascript:AndroidStreamsRunning()");
    }
    public static void Released(){
        WebView webView = MainActivity.instance.getWebView();
        if(webView == null)return;
        webView.loadUrl("javascript:AndroidReleased()");
    }
}
