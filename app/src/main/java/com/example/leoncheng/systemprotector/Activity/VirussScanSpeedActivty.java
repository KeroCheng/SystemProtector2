package com.example.leoncheng.systemprotector.Activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.leoncheng.systemprotector.AntiVirus.AntiVirusDao;
import com.example.leoncheng.systemprotector.AntiVirus.Info.ScanAppInfo;
import com.example.leoncheng.systemprotector.AntiVirus.ScanVirusAdapter;
import com.example.leoncheng.systemprotector.AntiVirus.Utils.MD5Utils;
import com.example.leoncheng.systemprotector.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.os.Handler;

/**
 * Created by Leon.Cheng on 2016/10/23.
 */
public class VirussScanSpeedActivty extends Activity implements View.OnClickListener {
    protected static final int SCAN_BENGIN = 100;
    protected static final int SCANING = 101;
    protected static final int SCAN_FINISH = 102;
    private int total;
    private int process;
    private PackageManager pm;
    private boolean flag;
    private boolean isStop;
    private RotateAnimation RAN;
    private ImageView mImgvLetfbtn;
    private TextView mTvScanproceess;
    private TextView mTvScanapp;
    private ListView mLvScanapp;
    private Button mBtnCanlescan;
    private ImageView mScanningIcon;
    private ScanVirusAdapter SVadapter;
    private List<ScanAppInfo> mScanAppInfos = new ArrayList<ScanAppInfo>();
    private SharedPreferences mSP;
    private ProgressBar mProgressBar;
    private Handler mHandler = new Handler() {
        public void handlerMessage(android.os.Message msg) {
            switch (msg.what) {
                case SCAN_BENGIN:
                    mTvScanapp.setText(" 初始化杀毒引擎中...");
                    break;
                case SCANING:
                    ScanAppInfo info = (ScanAppInfo) msg.obj;
                    mTvScanapp.setText("正在扫描:" + info.appName);
                    int speed = msg.arg1;
                    mTvScanproceess.setText((speed * 100 / total) + "%");
                    mScanAppInfos.add(info);
                    SVadapter.notifyDataSetChanged();
                    mLvScanapp.setSelection(mScanAppInfos.size());
                    break;
                case SCAN_FINISH:
                    mTvScanapp.setText("扫描完成!");
                    mScanningIcon.clearAnimation();
                    mBtnCanlescan.setText("查杀完成");
                    saveScanTime();
                    break;
            }
        }

        private void saveScanTime() {
            SharedPreferences.Editor edit = mSP.edit();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());
            currentTime = "上次查杀:" + currentTime;
            edit.putString("lastVirusScan", currentTime);
            edit.commit();
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virusscanspeed);
        pm = getPackageManager();
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        initView();
        scanVirus();
    }

    private void scanVirus() {
        flag = true;
        isStop = false;
        process = 0;
        mScanAppInfos.clear();
        new Thread() {
            public void run() {
                Message msg = Message.obtain();
                msg.what = SCAN_BENGIN;
                mHandler.sendMessage(msg);
                List<PackageInfo> installePackages = pm.getInstalledPackages(0);
                total = installePackages.size();
                for (PackageInfo info : installePackages) {
                    if (!flag) {
                        isStop = true;
                        return;
                    }
                    String apkpath = info.applicationInfo.sourceDir;//检查文件特征码
                    String md5info = MD5Utils.getFileMd5(apkpath);
                    String result = AntiVirusDao.checkVirus(md5info);
                    msg = Message.obtain();
                    msg.what = SCANING;
                    ScanAppInfo scanInfo = new ScanAppInfo();
                    if (result == null) {
                        scanInfo.description = "安全";
                        scanInfo.isVirus = false;
                    } else {
                        scanInfo.description = result;
                        scanInfo.isVirus = true;
                    }
                    process++;
                    scanInfo.packagename = info.packageName;
                    scanInfo.appName = info.applicationInfo.loadLabel(pm).toString();
                    scanInfo.appicon = info.applicationInfo.loadIcon(pm);
                    msg.obj = scanInfo;
                    msg.arg1 = process;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ;
                    }
                }
                msg = Message.obtain();
                msg.what = SCAN_FINISH;
                mHandler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private void initView() {
        findViewById(R.id.r1_tielbar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        mImgvLetfbtn = (ImageView) findViewById(R.id.imgv_letfbtn);
        ((TextView) findViewById(R.id.tv_title)).setText("查杀进度");
        mImgvLetfbtn.setOnClickListener(this);
        mImgvLetfbtn.setImageResource(R.drawable.leftjiantou);
        mTvScanproceess = (TextView) findViewById(R.id.tv_scanproceess);
        mTvScanapp = (TextView) findViewById(R.id.tv_scanapp);
        mLvScanapp = (ListView) findViewById(R.id.lv_scanapp);
        mBtnCanlescan = (Button) findViewById(R.id.btn_canlescan);
        mBtnCanlescan.setOnClickListener(this);
        mProgressBar=(ProgressBar)findViewById(R.id.ProgressBar);
        mProgressBar.setProgress(process);
        mProgressBar.getProgress();
        SVadapter = new ScanVirusAdapter(mScanAppInfos, this);
        mLvScanapp.setAdapter(SVadapter);
        mScanningIcon=(ImageView)findViewById(R.id.img_sanningicon);
        startAnim();
    }

    private void startAnim() {
        if (RAN == null) {
            RAN = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        RAN.setRepeatCount(Animation.INFINITE);
        RAN.setDuration(2000);
        mScanningIcon.startAnimation(RAN);
    }

    @Override

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_letfbtn:
                finish();
                break;
            case R.id.btn_canlescan:
                if(process==total&process>0){
                    finish();
                }else if(process>0&process<total&isStop==flag){
                    mScanningIcon.clearAnimation();//取消
                    flag=false;
                }else if(isStop){
                    startAnim();
                    scanVirus();
                }
                break;
        }
    }
    protected void  noDestroy(){
        flag=false;
        super.onDestroy();
    }
}
