package cn.chonor.lab8;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by Chonor on 2017/10/19.
 */

public class MyAdapter extends  BaseAdapter {
    private Context context;
    private List<Data> list;

    //同时适配两种界面 一种是购物车 一种是商品详情下方列表
    public MyAdapter(Context context, List<Data> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {//获得购居项列表的长度，也就是一共有多少个数据项。
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i) {//获得某一个数据项。
        if (list == null) {
            return 0;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }//获得数据项的位置。

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {//获得数据项的布局样式
        View convertView;
        ViewHolder viewHolder = new ViewHolder();
        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.birth = (TextView) convertView.findViewById(R.id.brith);
            viewHolder.gift = (TextView) convertView.findViewById(R.id.gift);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(list.get(i).getName());
        viewHolder.birth.setText(String.valueOf(list.get(i).getBirth()));
        viewHolder.gift.setText(list.get(i).getGift());

        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView birth;
        public TextView gift;
    }
}