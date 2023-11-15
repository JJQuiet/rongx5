package com.example.rongx5.util;


import android.os.Environment;

import com.example.rongx5.util.js.AppConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;

public class StorageConfig {
    private static String directory = "Download";

    private static void  update(String str){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//检查环境有无sd卡
            File sdCardDir=Environment.getExternalStorageDirectory();//获取sd卡的目录路径
            try {
                String dirPath = sdCardDir.getCanonicalPath()+File.separator+StorageConfig.directory;
//                if(!FileUtil.exist(dirPath))
//                {
//                    FileUtil.mkdir(dirPath);
//                }
                String filePath = dirPath+File.separator+"fslinconfig.txt";
//                if(FileUtil.exist(filePath))
//                {
//                    FileUtil.del(filePath);
//                }
                File destFile=new File(filePath);
//                RandomAccessFile raf=new RandomAccessFile(destFile,"rw");//随机访问文件内容
                FileWriter fileWriter =new FileWriter(destFile);
                fileWriter.write(str);  //写入空
                fileWriter.flush();
                fileWriter.close();

//                FileWriter writer = new FileWriter(destFile);
//                writer.write(str);

//                raf.seek(destFile.length());//文件定位到末尾
//                String str="hello";//转换成字符串
//                raf.write(str.getBytes());
//                raf.close();
            }
            catch (IOException e){
            }
        }
    }
    public static String read(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//检查环境有无sd卡
            File sdCardDir=Environment.getExternalStorageDirectory();//获取sd卡的目录路径
            try {
//                FileUtil.mkdir(sdCardDir.getCanonicalPath()+File.separator+StorageConfig.directory);
                String filePos = sdCardDir.getCanonicalPath()+File.separator+StorageConfig.directory+File.separator+"fslinconfig.txt";
                File destFile=new File(filePos);
//                Boolean isFile = FileUtil.isFile(destFile);
                if(destFile.exists() == false)
                {
                    return StorageConfig.create();
                }
                FileReader fileReader = new FileReader(destFile);
                String result = fileReader.readString();

                return result;
            }
            catch (IOException e){
                System.out.println();
            }
        }
        return "";
    }

    public static String saveConfig(String pbx,String fspbx,String leftHand){
        String template = "ip="+ AppConfig.ip+"\r\n";
        template += "fsip="+AppConfig.fsip+"\r\n";
        template += "pbx="+pbx+"\r\n";
        template += "fspbx="+fspbx+"\r\n";
        template += "leftHand="+leftHand+"\r\n";
        template += "rightHand=1010101010"+"\r\n";
        template += "handPwd="+AppConfig.handPwd+"\r\n";
        template += "httpPort=8002"+"\r\n";
        template += "socketPort=8001"+"\r\n";
        template += "conferName=3800"+"\r\n";
        StorageConfig.update(template);
        return template;
    }

    private static String create(){
        String template = "ip=192.168.0.47"+"\r\n";
        template += "fsip=192.168.0.47"+"\r\n";
        template += "pbx=192.168.0.150"+"\r\n";
        template += "fspbx=192.168.0.19"+"\r\n";
        template += "leftHand=1000"+"\r\n";
        template += "rightHand=10101010"+"\r\n";
        template += "handPwd=54321321"+"\r\n";
        template += "httpPort=8002"+"\r\n";
        template += "socketPort=8001"+"\r\n";
        template += "conferName=3800"+"\r\n";
        StorageConfig.update(template);
        return template;
    }

}


