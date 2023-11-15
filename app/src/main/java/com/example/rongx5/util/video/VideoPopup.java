package com.example.rongx5.util.video;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.rongx5.MainActivity;
import com.example.rongx5.R;

public class VideoPopup {
//    private static org.linphone.mediastream.video.capture.CaptureTextureView remoteView;
//    private static org.linphone.mediastream.video.capture.CaptureTextureView localView;
    private static View remoteGp;
    private static boolean _isInit = false;
    //我这里写在TTAdManagerHolder， 然后在AppActivity的oncreate中调用的
    public static void creatrView(){
        //原生的view直接添加到cocos的view内
        //R代表你是哪个包名的R文件，然后把创建好的view嵌入在你的cocosview里，不要使用setContentView(R.layout.activity_native_express);创建view
        View layout = LayoutInflater.from(MainActivity.instance).inflate(R.layout.video_popup, null);
        FrameLayout mFrameLayout = new FrameLayout(MainActivity.instance);
        mFrameLayout.addView(layout);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        lp.bottomMargin = 0;
        MainActivity.instance.addContentView(mFrameLayout,lp);
        View mExpressContainer = (View)MainActivity.instance.findViewById(R.id.videoGp);

//        View bottomContainer = (View)MainActivity.instance.findViewById(R.id.localVideoGp);
//        ViewGroup.MarginLayoutParams bottomparams = (ViewGroup.MarginLayoutParams)bottomContainer.getLayoutParams();
//        bottomparams = AndroidUnit.setCocosNodeSize(bottomparams,426,277,0,0);
//        bottomContainer.setLayoutParams(bottomparams);


        ImageButton outFull = MainActivity.instance.findViewById(R.id.imageButton);
        outFull.setOnClickListener((View view)->{
            //如果是小调度台，下面注释掉4行
//            Configuration mConfiguration = MainActivity.instance.getResources().getConfiguration();
//            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
//                MainActivity.instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
            VideoCall._ins.outFullScreen();
        });

        ImageButton cameraB = MainActivity.instance.findViewById(R.id.cameraBtn);
        cameraB.setOnClickListener((View view)->{
            VideoCall._ins.toggleCamera();
        });


        View remoteVideo = (View)MainActivity.instance.findViewById(R.id.remoteVideo);
        View localVideo = (View)MainActivity.instance.findViewById(R.id.selfVideo);


        VideoCall videoCall = new VideoCall().onCreateInit((TextureView) remoteVideo,(TextureView) localVideo,mExpressContainer);


//        VideoPopup.localView = (org.linphone.mediastream.video.capture.CaptureTextureView) localVideo;
//        VideoPopup.remoteView = (org.linphone.mediastream.video.capture.CaptureTextureView) remoteVideo;

        VideoPopup.remoteGp = (View)MainActivity.instance.findViewById(R.id.remoteGp);

        ImageButton rotationBtn = MainActivity.instance.findViewById(R.id.rotationBtn);
        rotationBtn.setOnClickListener((View view)->{
            Configuration mConfiguration = MainActivity.instance.getResources().getConfiguration();
            if (mConfiguration.orientation == mConfiguration.ORIENTATION_LANDSCAPE) {
                MainActivity.instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            else
            {
                MainActivity.instance.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

        });
        //        rotationBtn.setVisibility();
        //如果是小调度台，打开下面2行
        rotationBtn.setVisibility(View.GONE);
        cameraB.setVisibility(View.GONE);
//        _isInit  = true;
//        Configuration mConfiguration = MainActivity.instance.getResources().getConfiguration();
//        dispatchConfigChange(mConfiguration);
        //mFrameLayout.addView(view, layoutParams);
        //如已绘制SurfaceView则在surfaceView上一层绘制,必须addView之后使用
        //getGLSurfaceView().setZOrderMediaOverlay(true);
        //cocos的view是否在上层
        //getGLSurfaceView().setZOrderOnTop(true);
//        AppActivity._ins.getGLSurfaceView().getHolder().setFormat(PixelFormat.TRANSLUCENT);
//        mExpressContainer.setVisibility(View.GONE);
    }
    public static void dispatchConfigChange(Configuration newConfig){
        if(!_isInit)return;
        if (newConfig.orientation == newConfig.ORIENTATION_LANDSCAPE) {
//            VideoPopup.remoteGp.setRotation(90);

//            double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
//            double screenHei = (double) AndroidUnit.getScreenHight(MainActivity.instance);
////            ViewGroup.MarginLayoutParams remoteViewParams = (ViewGroup.MarginLayoutParams) VideoPopup.remoteGp.getLayoutParams();
////            remoteViewParams.width = (int)screenHei;
////            remoteViewParams.height = (int)screenHei;
////            VideoPopup.remoteGp.setLayoutParams(remoteViewParams);
//            VideoPopup.remoteView.setRotation(90);
////            VideoPopup.remoteView.setScaleX((int)screenWid/(int)screenHei);
//
//            VideoPopup.remoteView.setScaleY((float)screenWid/(float)screenHei/2);
////            VideoPopup.remoteView.setScaleY((int)screenHei/(int)screenWid);
//            VideoPopup.remoteView.setScaleX(1/2);
//            setupMatrix((int)screenWid,(int)screenHei,90,true);
        }
        else
        {
//            VideoPopup.remoteGp.setRotation(0);

//            double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
//            double screenHei = (double) AndroidUnit.getScreenHight(MainActivity.instance);
//            ViewGroup.MarginLayoutParams remoteViewParams = (ViewGroup.MarginLayoutParams) VideoPopup.remoteGp.getLayoutParams();
//            remoteViewParams.width = (int)screenWid;
//            remoteViewParams.height = (int)screenHei;
//            VideoPopup.remoteGp.setLayoutParams(remoteViewParams);
//            VideoPopup.remoteView.setRotation(0);
//            VideoPopup.remoteView.setScaleX(1);
//            VideoPopup.remoteView.setScaleY(1);
//            setupMatrix((int)screenWid,(int)screenHei,0,false);

        }
    }

//    public static void changeScreen(){
////        Configuration mConfiguration = MainActivity.instance.getResources().getConfiguration(); //获取设置的配置信息
////        int ori = mConfiguration.orientation; //获取屏幕方向
////        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {
//            double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
//            double screenHei = (double) AndroidUnit.getScreenHight(MainActivity.instance);
//
//            View remoteGp = (View)MainActivity.instance.findViewById(R.id.remoteGp);
//            ViewGroup.MarginLayoutParams remoteGpParams = (ViewGroup.MarginLayoutParams)remoteGp.getLayoutParams();
//            remoteGpParams.width = (int)screenWid;
//            remoteGpParams.height = (int)screenHei;
//            remoteGp.setLayoutParams(remoteGpParams);
//
//
//
//
//
////        } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {
////            //竖屏
////            double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
////            double cocosWid = 1920;
////            double cocosHei = 1180;
////            double needHei = cocosHei/cocosWid*screenWid;
////
////            View remoteGp = (View)MainActivity.instance.findViewById(R.id.remoteGp);
////            ViewGroup.MarginLayoutParams remoteGpParams = (ViewGroup.MarginLayoutParams)remoteGp.getLayoutParams();
////            remoteGpParams.width = (int)screenWid;
////            remoteGpParams.height = (int)needHei;
////            remoteGp.setLayoutParams(remoteGpParams);
////        }
//    }

//    private static void setupMatrix(int width, int height, int degrees, boolean isHorizontal) {
//        Matrix matrix = new Matrix();
//        //The video will be streched if the aspect ratio is in 1,5(recording at 480)
//        RectF src;
//        double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
//        double screenHei = (double) AndroidUnit.getScreenHight(MainActivity.instance);
//        if (isHorizontal)
//
////In my case, I changed this line, because with my onMeasure() and onLayout() methods my container view is already rotated and scaled, so I need to sent the inverted params to the src.
//            src = new RectF(0, 0,(int)screenWid, (int)screenHei);
//        else
//            src = new RectF(0, 0, (int)screenWid, (int)screenHei);
//
//        RectF dst = new RectF(0, 0, width, height);
//        RectF screen = new RectF(dst);
//        matrix.postRotate(degrees, screen.centerX(), screen.centerY());
//        matrix.mapRect(dst);
//
//        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
//        matrix.mapRect(src);
//
//        matrix.setRectToRect(screen, src, Matrix.ScaleToFit.FILL);
//        matrix.postRotate(degrees, screen.centerX(), screen.centerY());
//
//        VideoPopup.remoteView.setTransform(matrix);
//    }

}
