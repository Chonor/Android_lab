package cn.chonor.lab5;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class MyAdapter extends  BaseAdapter  {
    private Context context;
    private List<Good> list;
    private int choose;
    //同时适配两种界面 一种是购物车 一种是商品详情下方列表
    public MyAdapter(Context context, List<Good> list,int choose) {
        this.context = context;
        this.list = list;
        this.choose=choose;
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
            //创建View
            if(choose == 0) {//购物车时
                convertView = LayoutInflater.from(context).inflate(R.layout.shopping_cart, null);
                viewHolder.goodFirst = (TextView) convertView.findViewById(R.id.first1);
                viewHolder.goodName = (TextView) convertView.findViewById(R.id.name1);
                viewHolder.goodPrice = (TextView) convertView.findViewById(R.id.price1);
                viewHolder.goodCnt = (TextView) convertView.findViewById(R.id.cnt);
            }
            else {//商品下方列表时
                convertView = LayoutInflater.from(context).inflate(R.layout.good_info_item, null);
                viewHolder.goodName = (TextView) convertView.findViewById(R.id.info_textView);
            }
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(choose == 0) {//购物车时
            viewHolder.goodFirst.setText(list.get(i).getGoodName_First());
            viewHolder.goodName.setText(list.get(i).getGoodName());
            viewHolder.goodPrice.setText(list.get(i).getGoodPrice());
            viewHolder.goodCnt.setText(String.valueOf(list.get(i).getCnt()));
            if(list.get(i).getGoodName().equals("购物车"))//设置第一行
                viewHolder.goodCnt.setText("数量");
        }
        else{//商品下方列表时
            viewHolder.goodName.setText(list.get(i).getGoodName());
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView goodFirst;
        public TextView goodName;
        public TextView goodPrice;
        public TextView goodCnt;
    }
}