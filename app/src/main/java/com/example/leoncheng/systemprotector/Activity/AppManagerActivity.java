package com.example.leoncheng.systemprotector.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leoncheng.systemprotector.R;
import com.example.leoncheng.systemprotector.SoftwareManagement.AppManagerAtctivityAdapter;
import com.example.leoncheng.systemprotector.SoftwareManagement.Info.AppInfo;
import com.example.leoncheng.systemprotector.SoftwareManagement.Info.AppInfoParser;
import com.example.leoncheng.systemprotector.SoftwareManagement.Util.SDCardScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/9/19.
 */
public class AppManagerActivity extends Activity implements View.OnClickListener {
    private TextView mPhoneMemoryTV;//手机剩余内存TextView
    private TextView mSDMemoryTV;//展示SD卡剩余内存TextView
    private ListView mListView;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
    private List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();
    private AppManagerAtctivityAdapter adapter;
    /**
     * 接收应用程序卸载成功的广播
     **/
    private UninstallRecever recever;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    if (adapter == null) {
                        adapter = new AppManagerAtctivityAdapter(userAppInfos, systemAppInfos,
                                AppManagerActivity.this);
                    }
                    mListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case 15:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }

        ;
    };
    private TextView mAppNumTv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化布局
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_manager);
        //注册广播
        recever = new UninstallRecever();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package:");
        registerReceiver(recever, intentFilter);
        initView();
    }

    private void initView() {
        findViewById(R.id.r1_tielbar).setBackgroundColor(getResources()
                .getColor(R.color.bluegreen));
        ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_letfbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("软件管家");
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.leftjiantou);
        mPhoneMemoryTV = (TextView) findViewById(R.id.tv_phonememory_appmanager);
        mSDMemoryTV = (TextView) findViewById(R.id.tv_sdmemory_appmanager);
        mAppNumTv = (TextView) findViewById(R.id.tv_appnumber);
        mListView = (ListView) findViewById(R.id.lv_appmanager);
        getMemoryFromPhone();
        initData();
        initListener();
    }

    private void initListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (adapter != null) {
                    new Thread() {
                        public void run() {
                            AppInfo mappInfo = (AppInfo) adapter.getItem(position);
                            //记住当前条目的状态
                            boolean flag = mappInfo.isSelected;
                            //先将集合中所有条目的APPInfo变成未选中的状态
                            for (AppInfo appInfo : userAppInfos) {
                                appInfo.isSelected = false;
                            }
                            for (AppInfo appInfo : systemAppInfos) {
                                appInfo.isSelected = false;
                            }
                            if (mappInfo != null) {
                                //如果已经选中，则变为未选中
                                if (flag) {
                                    mappInfo.isSelected = false;
                                } else {
                                    mappInfo.isSelected = true;
                                }
                                mHandler.sendEmptyMessage(15);
                            }
                        }

                        ;
                    }.start();
                }
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }


            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= userAppInfos.size() + 1) {
                    mAppNumTv.setText("系统程序" + systemAppInfos.size() + "个");
                } else {
                    mAppNumTv.setText("用户程序" + userAppInfos.size() + "个");
                }
            }

        });

    }

    private void initData() {
        appInfos = new ArrayList<AppInfo>();
        new Thread() {
            public void run() {
                appInfos.clear();
                userAppInfos.clear();
                systemAppInfos.clear();
                appInfos.addAll(AppInfoParser.getAppInfos(AppManagerActivity.this));
                for (AppInfo appInfo : appInfos) {
                    //如果是用户App
                    if (appInfo.isUserApp) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(10);
            }
        }.start();
    }

    /**
     * 拿到手机和SD卡剩余内存
     **/
    public void getMemoryFromPhone() {
        File path = Environment.getExternalStorageDirectory(); //取得手机内存文件路径
        List<String> PathList=SDCardScanner.getExtSDCardPaths();
        if (path.exists()) {
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long avail_rom = stat.getAvailableBlocks();
            String str_avail_rom = Formatter.formatFileSize(AppManagerActivity.this, blockSize * avail_rom);
            mPhoneMemoryTV.setText("剩余手机内存：" + str_avail_rom);
        } else {
            mPhoneMemoryTV.setText("无内置SD卡");
        }
        if(PathList.size()>1){
            File TFpath=new File(PathList.get(0));
            StatFs stat = new StatFs(TFpath.getPath());
            long blockSize1 = stat.getBlockSize();
            long TFram = stat.getAvailableBlocks();
            String str_TFram = Formatter.formatFileSize(AppManagerActivity.this, blockSize1 * TFram);
            mSDMemoryTV.setText("剩余外置内存:"+str_TFram);
        }
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgv_letfbtn:
                finish();
                break;
        }
    }

    class UninstallRecever extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }

    protected void onDestroy() {
        unregisterReceiver(recever);
        recever = null;
        super.onDestroy();
    }
}
