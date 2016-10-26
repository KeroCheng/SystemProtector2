package com.example.leoncheng.systemprotector.SoftwareManagement.Info;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/9/20.
 */
public class AppInfo {
    public String packageName;
    /**
     * 包名
     */
    public Drawable icon;
    /**
     * 图标
     */
    public String appname;
    /**
     * 名称
     */
    public String apkPath;
    /**
     * 路径
     */
    public long appSize;
    /**
     * 大小
     */
    public boolean isInRoom;
    /**
     * 存储
     */
    public boolean isUserApp;
    /**
     * 判断是否为用户应用
     */
    public boolean isSelected = false;

    /**
     * 是否选中，默认false
     */
    public String getAppLocation(boolean isInRoom) {/**取App位置字符串*/
        if (isInRoom) {
            return "手机内存";
        } else {
            return "外部存储";
        }
    }

}
