package com.example.rongx5;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.EditText;

import com.example.rongx5.util.GlobalConfig;
import com.example.rongx5.util.JKeyBoard;
import com.example.rongx5.util.PermissUtil;
import com.example.rongx5.util.ScreenUnit;
import com.example.rongx5.util.StorageConfig;
import com.example.rongx5.util.WebViewJavaScriptFunction;
import com.example.rongx5.util.js.AppConfig;
import com.example.rongx5.util.js.NativeUnit;
import com.example.rongx5.util.video.VideoPopup;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.export.external.extension.proxy.ProxyWebChromeClientExtension;
import com.tencent.smtt.export.external.interfaces.GeolocationPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.MediaAccessPermissionsCallback;
import com.tencent.smtt.export.external.interfaces.PermissionRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Policy;
import java.util.HashMap;
import android.hardware.Camera;

import org.linphone.core.tools.service.CoreService;

import cn.hutool.core.util.StrUtil;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";

    public static MainActivity instance = null;

    private WindowManager windowManager;
    private View fullScreenLayer;

    private   WebView mWebView;

//    private static  String mHomeUrl = "file:///android_asset/webpage/index.html";
    private static  String mHomeUrl = "file:///android_asset/app/index.html";
//    private static  String mHomeUrl = "http://192.168.0.7:8080";
//    private static  String mHomeUrl = "https://yongling8808.github.io/test/video_demo/video.html";

    private void fullScreen(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent startIntent = new Intent(this, CoreService.class);
        startService(startIntent);

        setContentView(R.layout.activity_main);

        PermissUtil.verifyPermissions(this);

        windowManager = getWindowManager();

        this.initTbsSetting();
        this.initTbsEnvir();
        this.initwebview();

        JKeyBoard.instance().setContext(this);

        MainActivity.instance = this;

        MainActivity.instance.initIpConfig();

        ScreenUnit.wark(this);

        VideoPopup.creatrView();
    }
    public void handevent(int state){
        if(this.mWebView != null)
        {
            this.mWebView.loadUrl("javascript:AndroidEventListenerFun("+state+")");
        }
    }

    private void initwebview(){
        WebView mWebView = (com.tencent.smtt.sdk.WebView) findViewById(R.id.forum_context);
        this.mWebView = mWebView;
        this.initwebviewSetting(mWebView);
        this.initWebViewClient(mWebView);
        this.initWebChromeClient(mWebView);
        this.initWebChromeClientExten(mWebView);
        this.initJavaScriptInterface();
        boolean isX5 = mWebView.getIsX5Core();
        if(!isX5)
        {
            mWebView.loadUrl("http://debugtbs.qq.com");
            return;
        }
        mWebView.loadUrl(mHomeUrl);
    }
    @Override
    protected void onResume() {
        super.onResume();
//        try {
//            final CameraManager manager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
//            final String[] list = manager.getCameraIdList();
//            System.out.printf("");
//        } catch (CameraAccessException e) {
//
//        }

        // 如果之前处于全屏状态，重新进入后需要再次调用全屏
        if (fullScreenLayer != null) {
            fullScreen(fullScreenLayer);
        }
    }
    private void initwebviewSetting( WebView mWebView){
        WebSettings webSetting = mWebView.getSettings();
        mWebView.getSettings().setJavaScriptEnabled(true);// 支持js
        mWebView.getSettings().setUseWideViewPort(true); //自适应屏幕

        webSetting.setAllowFileAccess(true);
        webSetting.setSupportZoom(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);

        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setMediaPlaybackRequiresUserGesture(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);


    }
    private void initTbsSetting(){
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
    }
    private void initTbsEnvir(){
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                // 内核初始化完成，可能为系统内核，也可能为系统内核
            }
            /**
             * 预初始化结束
             * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候，默认会回调false，此时将会使用系统内核代替
             * @param isX5 是否使用X5内核
             */
            @Override
            public void onViewInitFinished(boolean isX5) {

            }
        });
    }
    private void initWebViewClient(WebView mWebView) {
        mWebView.setWebViewClient(new WebViewClient() {
            /**
             * 具体接口使用细节请参考文档：
             * https://x5.tencent.com/docs/webview.html
             * 或 Android WebKit 官方：
             * https://developer.android.com/reference/android/webkit/WebChromeClient
             */

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(TAG, "onPageStarted, view:" + view + ", url:" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "onPageFinished, view:" + view + ", url:" + url);
            }

            @Override
            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "onReceivedError: " + errorCode
                        + ", description: " + description
                        + ", url: " + failingUrl);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                if (webResourceRequest.getUrl().toString().contains("debugdebug")) {
                    InputStream in = null;
                    Log.i("AterDebug", "shouldInterceptRequest");
                    try {
                        in = new FileInputStream(new File("/sdcard/1.png"));
                    } catch (Exception e) {

                    }

                    return new WebResourceResponse("image/*", "utf-8", in);
                } else {
                    return super.shouldInterceptRequest(webView, webResourceRequest);
                }

            }
        });
    }
    private void initWebChromeClient(WebView mWebView) {
        final Context context = this;
        final Activity activity = this;
        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * 具体接口使用细节请参考文档：
             * https://x5.tencent.com/docs/webview.html
             * 或 Android WebKit 官方：
             * https://developer.android.com/reference/android/webkit/WebChromeClient
             */

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "onProgressChanged, newProgress:" + newProgress + ", view:" + view);
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                new AlertDialog.Builder(context).setTitle("JS弹窗Override")
                        .setMessage(message)
                        .setPositiveButton("OK", (dialogInterface, i) -> result.confirm())
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView webView, String url, String message, JsResult result) {
                new AlertDialog.Builder(context).setTitle("JS弹窗Override")
                        .setMessage(message)
                        .setPositiveButton("OK", (dialogInterface, i) -> result.confirm())
                        .setNegativeButton("Cancel", (dialogInterface, i) -> result.cancel())
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsBeforeUnload(WebView webView, String url, String message, JsResult result) {
                new AlertDialog.Builder(context).setTitle("页面即将跳转")
                        .setMessage(message)
                        .setPositiveButton("OK", (dialogInterface, i) -> result.confirm())
                        .setNegativeButton("Cancel", (dialogInterface, i) -> result.cancel())
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView webView, String url, String message, String defaultValue, JsPromptResult result) {
                String ipconfigStr = GlobalConfig.ipconfig;
                String laststr = ipconfigStr.replaceAll("\r\n",",");
                result.confirm(laststr);
                return true;
            }

            /**
             * Return value usage see FILE_CHOOSE_REQUEST in
             * {@link(int, int, Intent)}
             */
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                Log.i(TAG, "openFileChooser: " + fileChooserParams.getMode());
                return true;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissionsCallback geolocationPermissionsCallback) {
//                if (verifyLocationPermissions(activity)) {
//                    geolocationPermissionsCallback.invoke(origin, true, false);
//                } else {
//                    locationPermissionUrl = origin;
//                    mGeolocationCallback = geolocationPermissionsCallback;
//                }
            }
            @Override
            public void onPermissionRequest(PermissionRequest permissionRequest) {
                //super.onPermissionRequest(permissionRequest);
                permissionRequest.grant(permissionRequest.getResources());
                Log.e(TAG,"onPermissionRequest");
            }
            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
                // view 为内核生成的全屏视图，需要添加到相应的布局位置（如：全屏幕）
                // customViewCallback 用于主动控制全屏退出
                windowManager.addView(view, new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_APPLICATION));
                fullScreen(view);
                fullScreenLayer = view;
            }

            @Override
            public void onHideCustomView() {
                windowManager.removeViewImmediate(fullScreenLayer);
                fullScreenLayer = null;
            }
        });
    }
    private void initWebChromeClientExten(WebView mWebView){
        mWebView.setWebChromeClientExtension(new ProxyWebChromeClientExtension(){
            @Override
            public boolean onPermissionRequest(String s, long l, MediaAccessPermissionsCallback mediaAccessPermissionsCallback) {
                long allowed = 0;
                allowed = allowed | MediaAccessPermissionsCallback.ALLOW_AUDIO_CAPTURE;
                boolean retain = true;
                mediaAccessPermissionsCallback.invoke(s, allowed,retain);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        System.exit(0);
    }
    private void initIpConfig(){
        String ipConfig = StorageConfig.read();
        if(StrUtil.isEmpty(ipConfig) == false)
        {
            GlobalConfig.ipconfig = ipConfig;
            AppConfig.init();
        }
    }
    private void initJavaScriptInterface() {
        final Activity context = this;
        mWebView.addJavascriptInterface(new WebViewJavaScriptFunction() {
            @Override
            public void onJsFunctionCalled(String tag) {
            }
            @JavascriptInterface
            public void warkScreen() {
                ScreenUnit.wark(MainActivity.instance);
            }
            @JavascriptInterface
            public void x5linphoneSendMsg(String json) {
                NativeUnit.receive(json);
            }

        }, "Android");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenUnit.release();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        VideoPopup.dispatchConfigChange(newConfig);

    }

    public WebView getWebView(){
        return this.mWebView;
    }
}