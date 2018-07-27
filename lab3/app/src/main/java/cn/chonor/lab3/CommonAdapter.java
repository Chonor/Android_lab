package cn.chonor.lab3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Chonor on 2017/10/21.
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    private Context mContext;
    private List<Good> mDatas;
    private int mLayoutId;
    private boolean flag;
    //构造函数
    public CommonAdapter(Context mContext,int mLayoutId,List<Good>mDatas,boolean flag) {
        this.mLayoutId=mLayoutId;
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.flag=flag;
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
    public void convert(ViewHolder viewHolder, Good good){
        if(flag) {
            TextView first = viewHolder.getView(R.id.first);
            first.setText(good.getGoodName_First());
            TextView name = viewHolder.getView(R.id.name);
            name.setText(good.getGoodName());
        }else{
            ImageView imageView = viewHolder.getView(R.id.image2);
            String names=good.getGoodName();
            if(names.equals("Enchated Forest"))imageView.setImageResource(R.mipmap.enchatedforest);
            else if(names.equals("Arla Milk"))imageView.setImageResource(R.mipmap.arla);
            else if(names.equals("Devondale Milk"))imageView.setImageResource(R.mipmap.devondale);
            else if(names.equals("Kindle Oasis"))imageView.setImageResource(R.mipmap.kindle);
            else if(names.equals("waitrose 早餐麦片"))imageView.setImageResource(R.mipmap.waitrose);
            else if(names.equals("Mcvitie's 饼干"))imageView.setImageResource(R.mipmap.mcvitie);
            else if(names.equals("Ferrero Rocher"))imageView.setImageResource(R.mipmap.ferrero);
            else if(names.equals("Maltesers"))imageView.setImageResource(R.mipmap.maltesers);
            else if(names.equals("Lindt"))imageView.setImageResource(R.mipmap.lindt);
            else if(names.equals("Borggreve"))imageView.setImageResource(R.mipmap.borggreve);
            TextView name = viewHolder.getView(R.id.name2);
            name.setText(names);
        }
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