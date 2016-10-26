package com.example.leoncheng.systemprotector.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.leoncheng.systemprotector.Home.HomeAdapter;
import com.example.leoncheng.systemprotector.R;
/**
 * Created by Administrator on 2016/9/13.
 */
public class HomeActivity extends Activity {
    private GridView homeView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        //初始化表格布局
        homeView = (GridView) findViewById(R.id.homeView);
        homeView.setAdapter(new HomeAdapter(HomeActivity.this));
        //设置条目点击
        homeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent 代表gridView，View代表每个条目对象，position代表每个条目的位置
                switch (position) {
                    case 0://清理垃圾
                        break;
                    case 1://进程管理
                        break;
                    case 2://流量统计
                        break;
                    case 3://杀毒防护
                        startActivity(VirusScanActivity.class);
                        break;
                    case 4://软件管家
                        startActivity(AppManagerActivity.class);
                        break;
                    case 5://通讯防卫
                        break;
                    case 6://手机防盗
                        //if (isSetUpPassword()){
                        //弹出密码对话框
                        //showInterPswdDialog();
                        // }else {
                        //弹出设置密码对话框
                        //showSetUppswdDialog();
                        // }
                        break;
                    case 7://进程管理
                        break;
                    case 9://进程管理
                        break;
                }

            }

        });
    }
    public void  startActivity(Class<?> cls){
        Intent intent=new Intent (HomeActivity.this,cls);
        startActivity(intent);
    }
}
