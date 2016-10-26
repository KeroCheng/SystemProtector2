package com.example.leoncheng.systemprotector.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leoncheng.systemprotector.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/20.
 */
public class VirusScanActivity extends Activity implements View.OnClickListener {

    private SharedPreferences mSP;
    private TextView mTvLastcantime;
    private ImageView mImgvLetfbtn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_virusscan);
        mSP = getSharedPreferences("config", MODE_PRIVATE);
        copyDB("antivirus.db");
        initView();
    }

    protected void onResume() {
        String string = mSP.getString("lastVirusScan", "您还未曾查杀病毒！");
        mTvLastcantime.setText(string);
        super.onResume();
    }

    private void copyDB(final String dbname) {
        new Thread() {
            public void run() {
                try {
                    File file = new File(getFilesDir(), dbname);
                    if (file.exists() && file.length() > 0) {
                        Log.i("ViruScanActivity", "数据库已存在！");
                        return;
                    }
                    InputStream is = getAssets().open(dbname);
                    FileOutputStream fos = openFileOutput(dbname, MODE_PRIVATE);
                    byte[] buffter = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffter)) != -1) {
                        fos.write(buffter, 0, len);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgv_letfbtn:
                finish();
                break;
            case R.id.r1_allscanvirus:
                startActivity(new Intent(this,VirussScanSpeedActivty.class));
                break;
        }

    }

    private void initView() {
        findViewById(R.id.r1_tielbar).setBackgroundColor(getResources().getColor(R.color.light_blue));
        ((TextView)findViewById(R.id.tv_title)).setText("病毒查杀");
        mImgvLetfbtn = (ImageView) findViewById(R.id.imgv_letfbtn);
        mImgvLetfbtn.setOnClickListener(this);
        mImgvLetfbtn.setImageResource(R.drawable.leftjiantou);
        mTvLastcantime = (TextView) findViewById(R.id.tv_lastcantime);
        findViewById(R.id.r1_allscanvirus).setOnClickListener(this);
    }
}
