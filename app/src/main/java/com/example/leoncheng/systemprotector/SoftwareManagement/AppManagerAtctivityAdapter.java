package com.example.leoncheng.systemprotector.SoftwareManagement;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leoncheng.systemprotector.R;
import com.example.leoncheng.systemprotector.SoftwareManagement.Info.AppInfo;
import com.example.leoncheng.systemprotector.SoftwareManagement.Util.DensityUtil;
import com.example.leoncheng.systemprotector.SoftwareManagement.Util.EngineUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/10/15.
 */
public class AppManagerAtctivityAdapter extends BaseAdapter {

    private List<AppInfo> UserAppInfos;
    private List<AppInfo> SystemAppInfos;
    private Context mContext;

    public AppManagerAtctivityAdapter(List<AppInfo> userAppInfos, List<AppInfo> systemAppInfos, Context mContext){
        super();
        UserAppInfos=userAppInfos;
        SystemAppInfos=systemAppInfos;
        this.mContext=mContext;
    }


    @Override
    public int getCount() {
        //因为有两个条目需要用于显示用户进程,因此系统进程需要加2
        return UserAppInfos.size()+SystemAppInfos.size()+2;
    }

    @Override
    public Object getItem(int position) {
        if ( position==0 ){
            //第0个位置显示的应该是用户程序个数的标签
            return null;
        }else if (position==(UserAppInfos.size()+1)){
            return null;
        }
        AppInfo appInfo;
        if (position<(UserAppInfos.size()+1)){
            //用户程序
            appInfo=UserAppInfos.get(position-1);
            //多了一个textview标签,位置-1
        }else {
            //系统程序
            int location=position-UserAppInfos.size()-2;
            appInfo=SystemAppInfos.get(location);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position==0){
            TextView mTextView=getTextView();
            mTextView.setText("用户程序："+UserAppInfos.size()+
                    "个");
            return mTextView;
        }else if (position==(UserAppInfos.size()+1)){
            TextView mTextView=getTextView();
            mTextView.setText("系统程序："+SystemAppInfos.size()+
                    "个");
            return mTextView;
        }
        //获取到当前的App对象
        AppInfo appInfo;
        if (position<(UserAppInfos.size()+1)){
            appInfo=UserAppInfos.get(position-1);
        }else {
            appInfo=SystemAppInfos.get(position-UserAppInfos.size()-2);
        }
        ViewHolder mViewHolder;
        if (convertView!=null&convertView instanceof LinearLayout){
            mViewHolder=(ViewHolder) convertView.getTag();
        }else {
            mViewHolder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.item_appmanager_list,null);
            mViewHolder.mAppIconImgv=(ImageView) convertView.findViewById(R.id.imgv_appicon);
            mViewHolder.mAppLocationTV=(TextView) convertView.findViewById(R.id.tv_appisrom);
            mViewHolder.mAppSizeTV=(TextView) convertView.findViewById(R.id.tv_appsize);
            mViewHolder.mAppNameTV=(TextView) convertView.findViewById(R.id.tv_appname);
            mViewHolder.mLuanchAppTv=(TextView) convertView.findViewById(R.id.tv_launch_app);
            mViewHolder.mSettingAppTV=(TextView) convertView.findViewById(R.id.tv_setting_app);
            mViewHolder.mShareAppTV=(TextView) convertView.findViewById(R.id.tv_share_app);
            mViewHolder.mUninstallTV=(TextView) convertView.findViewById(R.id.tv_uninstall_app);
            mViewHolder.mAppOptionLL=(LinearLayout) convertView.findViewById(R.id.ll_optin_app);
            convertView.setTag(mViewHolder);
        }
        if ( appInfo!=null ){
            mViewHolder.mAppLocationTV.setText(appInfo.getAppLocation(appInfo.isInRoom));
            mViewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
            mViewHolder.mAppSizeTV.setText(android.text.format.Formatter.formatFileSize(mContext,appInfo.appSize));
            mViewHolder.mAppNameTV.setText(appInfo.appname);
            if (appInfo.isSelected){
                mViewHolder.mAppOptionLL.setVisibility(View.VISIBLE);
            }else {
                mViewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
        }
        MyClickListener listener=new MyClickListener(appInfo);
        mViewHolder.mLuanchAppTv.setOnClickListener(listener);
        mViewHolder.mSettingAppTV.setOnClickListener(listener);
        mViewHolder.mShareAppTV.setOnClickListener(listener);
        mViewHolder.mUninstallTV.setOnClickListener(listener);
        return convertView;
    }

    private TextView getTextView() {
        TextView mTextView=new TextView(mContext);
        mTextView.setBackgroundColor(mContext.getResources().getColor(R.color.graye5));
        mTextView.setPadding(DensityUtil.dip2px(mContext,5),DensityUtil.dip2px(mContext,5)
                ,DensityUtil.dip2px(mContext,5),DensityUtil.dip2px(mContext,5));
        mTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        return mTextView;
    }

    static class ViewHolder{
        /** 启动App */
        TextView mLuanchAppTv;
        /** 卸载App */
        TextView mUninstallTV;
        /** 分享App */
        TextView mShareAppTV;
        /** 设置App */
        TextView mSettingAppTV;
        /** app图标*/
        ImageView mAppIconImgv;
        /** app位置 */
        TextView mAppLocationTV;
        /** app大小 */
        TextView mAppSizeTV;
        /** app名称 */
        TextView mAppNameTV;
        /** 操作App的线性布局 */
        LinearLayout mAppOptionLL;
    }
    class MyClickListener implements View.OnClickListener{
        private  AppInfo appInfo;
        public MyClickListener(AppInfo appInfo){
            super();
            this.appInfo=appInfo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_launch_app:
                    //启动应用
                    EngineUtils.starApplication(mContext,appInfo);
                    break;
                case R.id.tv_share_app:
                    //分享应用
                    EngineUtils.shareApplication(mContext,appInfo);
                    break;
                case R.id.tv_setting_app:
                    //设置应用
                    EngineUtils.SettingAppDetail(mContext,appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    //卸载应用,需要注册广播接收者
                    if ( appInfo.packageName.equals(mContext.getPackageName())){
                        Toast.makeText(mContext,"您没有权限卸载此应用",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.uninstallApplicantion(mContext,appInfo);
                    break;
            }
        }
    }}
