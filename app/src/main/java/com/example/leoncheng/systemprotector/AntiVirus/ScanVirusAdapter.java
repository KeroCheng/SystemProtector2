package com.example.leoncheng.systemprotector.AntiVirus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leoncheng.systemprotector.AntiVirus.Info.ScanAppInfo;
import com.example.leoncheng.systemprotector.R;

import java.util.List;

/**
 * Created by Leon.Cheng on 2016/10/24.
 */
public class ScanVirusAdapter extends BaseAdapter{
    private List<ScanAppInfo> mScanAppInfos;
    private Context context;
    public ScanVirusAdapter(List<ScanAppInfo>  scanAppInfo,Context context){
        super();
        mScanAppInfos=scanAppInfo;
        this.context=context;
    }
    @Override
    public int getCount() {
        return mScanAppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mScanAppInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            //由于程序锁的条目与病毒扫描内容基本一致，因此可以重用程序锁的Item布局
            convertView=View.inflate(context, R.layout.item_list_applock,null);
            holder=new ViewHolder();
            holder.mAppIconImgv=(ImageView)convertView.findViewById(R.id.imgv_appicon);
            holder.mAppnameTV=(TextView)convertView.findViewById(R.id.tv_appname);
            holder.mSanIconImgv=(ImageView)convertView.findViewById(R.id.imgv_lock);
            convertView.setTag(holder);
        }else {
            holder=(ViewHolder)convertView.getTag();
        }
        ScanAppInfo scanAppInfo=mScanAppInfos.get(position);
        if (!scanAppInfo.isVirus){
            holder.mSanIconImgv.setBackgroundResource(R.drawable.blue_right_icon);
            holder.mAppnameTV.setTextColor(context.getResources().getColor(R.color.black));
            holder.mAppnameTV.setText(scanAppInfo.appName);
        }else {
            holder.mAppnameTV.setTextColor(context.getResources().getColor(R.color.bright_red));
            holder.mAppnameTV.setText(scanAppInfo.appName+"("+scanAppInfo.description+")");
        }
        holder.mAppIconImgv.setImageDrawable(scanAppInfo.appicon);
        return convertView;
    }
    static class ViewHolder{
        ImageView  mAppIconImgv;
        TextView mAppnameTV;
        ImageView mSanIconImgv;
    }
}
