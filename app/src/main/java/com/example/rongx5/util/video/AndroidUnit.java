package com.example.rongx5.util.video;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.rongx5.MainActivity;


public class AndroidUnit {
    //根据手机的分辨率从dp单位转化为px单位
    public static int dip2px(Context context, float dpValue) {
        //获取当前手机的像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
        //四舍五入
        return (int) (dpValue * scale + 0.5f);
    }

    //根据手机的分辨率从px单位转化为dp单位
    public static int px2dip(Context context, float pxValue) {
        //获取当前手机的像素密度
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //获取屏幕宽度
    public static int getScreenWidth(Context ctx) {
        //从系统服务中获取获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    //获取屏幕高度
    public static int getScreenHight(Context ctx) {
        //从系统服务中获取获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static float getScreenDensity(Context ctx) {
        //从系统服务中获取获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        //从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    public static ViewGroup.MarginLayoutParams setCocosNodeSize(ViewGroup.MarginLayoutParams params, double wid, double hei, double x, double y) {
        double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
        double screenHei = (double) AndroidUnit.getScreenHight(MainActivity.instance);
        double cocosWid = 1920;
        double cocosHei = 1080;
        double needHei = cocosHei / cocosWid * screenWid;
        double paddtop = (screenHei - needHei) / 2;
        double scale = screenWid / cocosWid;
        wid = wid * scale;
        hei = hei * scale;
        x = x * scale;
        y = y * scale + paddtop;
        params.width = (int) wid;
        params.height = (int) hei;
//        params.leftMargin = (int)x;
//        params.topMargin = (int)y;
        return params;
    }

    public static ViewGroup.MarginLayoutParams setCocosHeightNodeSize(ViewGroup.MarginLayoutParams params, double wid, double hei, double x, double y) {
        double screenWid = (double) AndroidUnit.getScreenWidth(MainActivity.instance);
        double screenHei = (double) AndroidUnit.getScreenHight(MainActivity.instance);
        double cocosWid = 1920;
        double cocosHei = 1080;
        double needWid = cocosWid / cocosHei * screenHei;
        double paddleft = (screenWid - needWid) / 2;
        double scale = screenHei / cocosHei;
        wid = wid * scale;
        hei = hei * scale;
        x = x * scale + paddleft;
        y = y * scale;
        params.width = (int) wid;
        params.height = (int) hei;
        params.leftMargin = (int) x;
        params.topMargin = (int) y;
        return params;
    }

}
