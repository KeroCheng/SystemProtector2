package com.example.leoncheng.systemprotector.Home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leoncheng.systemprotector.R;


/**
 * Created by Administrator on 2016/9/12.
 */
public class HomeAdapter extends BaseAdapter {
    int[] image = {R.drawable.qingli, R.drawable.jinchengguanli, R.drawable.liuliangtongji, R.drawable.shadu, R.drawable.rjguanjia, R.drawable.tongxun, R.drawable.fangdao, R.drawable.gongju, R.drawable.set
    };
    String[] iconId = {"清理垃圾", "进程管理", "流量统计", "杀毒防护", "软件管家", "通讯防卫", "手机防盗", "高级工具", "设 置"
    };
    private Context context;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 9;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_main, null);
        ImageView home_icon = (ImageView) view.findViewById(R.id.home_icon);
        TextView h_iconname = (TextView) view.findViewById(R.id.h_iconname);
        home_icon.setImageResource(image[position]);
        h_iconname.setText(iconId[position]);
        return  view;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
