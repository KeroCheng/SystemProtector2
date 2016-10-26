package com.example.leoncheng.systemprotector.SoftwareManagement.Info;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class AppInfoParser {
    //获取所有的应用
    public static List<AppInfo> getAppInfos(Context context) {
        PackageManager Pm = context.getPackageManager();
        List<PackageInfo> packageInfos = Pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packInfo : packageInfos) {
            AppInfo appInfo = new AppInfo();
            String packname = packInfo.packageName;
            appInfo.packageName = packname;
            Drawable icon = packInfo.applicationInfo.loadIcon(Pm);
            appInfo.icon = icon;
            String appname = packInfo.applicationInfo.loadLabel(Pm).toString();
            appInfo.appname = appname;
            //Apk包的路径
            String apkpath = packInfo.applicationInfo.sourceDir;
            appInfo.apkPath = apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appInfo.appSize = appSize;
            //安装位置
            int flags = packInfo.applicationInfo.flags;

            if (appInfo.isInRoom = (ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0){
                appInfo.isInRoom=false;
            }else{
                appInfo.isInRoom=true;
            }
            if (appInfo.isUserApp = (ApplicationInfo.FLAG_SYSTEM & flags) != 0){
                appInfo.isUserApp=false;
            }else{
                appInfo.isUserApp=true;
            }
            appInfos.add(appInfo);
            appInfo=null;
        }
        return appInfos;
    }
}
