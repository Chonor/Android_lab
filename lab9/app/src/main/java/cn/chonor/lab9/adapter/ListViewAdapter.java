package cn.chonor.lab9.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

import cn.chonor.lab9.R;
import cn.chonor.lab9.model.Repositories;

/**
 * Created by Chonor on 2017/10/19.
 */

public class ListViewAdapter extends  BaseAdapter  {
    private Context context;
    private List<Repositories> list;

    public ListViewAdapter(Context context, List<Repositories> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {//获得列表的长度，也就是一共有多少个数据项。
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
            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.repository_name);
            viewHolder.language = (TextView) convertView.findViewById(R.id.language);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.language.setText(list.get(i).getLanguage());
        viewHolder.description.setText(list.get(i).getDescription());
        return convertView;
    }

    private class ViewHolder {
        public TextView name;
        public TextView description;
        public TextView language;
    }
}