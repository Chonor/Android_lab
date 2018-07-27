package cn.chonor.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.chonor.lab9.model.Github;
import cn.chonor.lab9.R;

/**
 * Created by Chonor on 2017/10/21.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    private List<Github> mDatas;
    private int mLayoutId;
    //构造函数
    public RecyclerViewAdapter(Context mContext, int mLayoutId, List<Github>mDatas) {
        this.mLayoutId=mLayoutId;
        this.mContext = mContext;
        this.mDatas = mDatas;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup,int viewType) {
        ViewHolder viewHolder=ViewHolder.get(mContext,viewGroup,mLayoutId);
        return  viewHolder;
    }
    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        convert(viewHolder,mDatas.get(position));
        if(mOnItemClickListener !=null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    mOnItemClickListener.onClick(viewHolder.getAdapterPosition());
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mOnItemClickListener.onLongClick(viewHolder.getAdapterPosition());
                    return false;
                }
            });
        }
    }
    public void convert(ViewHolder viewHolder, Github github){
        TextView name = viewHolder.getView(R.id.name);
        name.setText(github.getLogin());
        TextView id = viewHolder.getView(R.id.id);
        id.setText("id: "+String.valueOf(github.getId()));
        TextView blog = viewHolder.getView(R.id.blog);
        blog.setText("blog: "+github.getBlog());
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    //注册点击事件
    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }
    private OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mConvertView;//存储 list_Irem
        private SparseArray<View>mView;//存储 list_Item的子View
        public ViewHolder(Context context,View view,ViewGroup viewGroup){
            super(view);
            mConvertView=view;
            mView = new SparseArray<View>();
        }
        //获取viewHolder实例
        public  static ViewHolder get (Context context,ViewGroup viewGroup,int layoutId){
            View itemView = LayoutInflater.from(context).inflate(layoutId,viewGroup,false);
            ViewHolder viewHolder = new ViewHolder(context,itemView,viewGroup);
            return viewHolder;
        }
        //viewHolder尚未将子View缓存到SparseArray数组中时仍然需要通过findViewByld()创建View对象如果已缓存直接返回
        public <T extends View>T getView(int viewId){
            View view=mView.get(viewId);
            if(view == null){
                view=mConvertView.findViewById(viewId);
                mView.put(viewId,view);
            }
            return (T)view;
        }
    }
}