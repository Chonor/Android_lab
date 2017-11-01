package cn.chonor.lab5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by Chonor on 2017/10/20.
 */

public class Shoppingcart extends AppCompatActivity {
    private ListView listView;
    private Data data = new Data();
    private FloatingActionButton floatingActionButton = null;
    private AlertDialog.Builder builder = null;
    private MyAdapter myAdapter;
    private TextView all;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);
        init();//初始化
        get_set_data();//接收数据
        init_listener();//事件监听
        EventBus.getDefault().register(this);
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        get_set_data();//获取数据
    }
    private void get_set_data(){ //接收数据
        Bundle extras_main = getIntent().getBundleExtra("mainActivity");
        if(extras_main != null) {  //主界面
            ArrayList<Good> tmp = extras_main.getParcelableArrayList("data"); //获得数据
            ArrayList<Good> tmp1 = extras_main.getParcelableArrayList("cart");
            data.setGood_list(tmp);//设置数据
            data.setCart_list(tmp1);
        }
        //购物车时增加一列空
        if (data.getCart_list().size() == 0 || !data.getCart_list().get(0).getGoodName().equals("购物车")) {
                Good good = new Good("购物车", "价格", " ", " ");
            good.setGoodFisrt("*");
            data.addCart_list(0, good);
        }
        double sum=0;
        for(int i=1;i<data.getCart_list().size();i++){
            sum+= data.getCart_list_index(i).getCnt()* data.getCart_list_index(i).getPrices();
        }
        all.setText("总价：￥ "+ String.valueOf(sum));
        myAdapter = new MyAdapter(this, data.getCart_list(), 0);
        listView.setAdapter(myAdapter);
    }
    private void init(){//初始化
        builder = new AlertDialog.Builder(this);
        all=(TextView)findViewById(R.id.all);
        listView = (ListView) this.findViewById(R.id.myListView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton1);
    }
    private void init_listener(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() { //按钮转跳
            @Override
            public void onClick(View view) { //设置数据
                Intent intent = new Intent(Shoppingcart.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data", data.getGood_list());
                bundle.putParcelableArrayList("cart", data.getCart_list());
                intent.putExtra("shoppingcart", bundle);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//单击进入详细
                if (data.getCart_list().get(i).getGoodName().equals("购物车")) {
                } else {//数据传输
                    Intent intent = new Intent(Shoppingcart.this, Good_Info.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putParcelableArrayList("data", data.getGood_list());
                    bundle.putParcelableArrayList("cart", data.getCart_list());
                    intent.putExtra("shoppingcart", bundle);
                    startActivity(intent);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按提示删除
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (data.getCart_list().get(i).getGoodName().equals("购物车")) {
                } else {
                    alertdialog_build(i);
                    AlertDialog dialog = builder.create(); //完成创建AlertDialog并显示
                    dialog.show();
                    double sum=0;
                    for(int j=1;j<data.getCart_list().size();j++){
                        sum+= data.getCart_list_index(j).getCnt()* data.getCart_list_index(j).getPrices();
                    }
                    all.setText("总价：￥ "+ String.valueOf(sum));
                }
                return true;
            }
        });
    }
    public void alertdialog_build(final int position) {
        //alertdialog 初始化
        builder.setTitle("移除商品 ");
        final String tmp = data.getCart_list().get(position).getGoodName();
        builder.setMessage("从购物车移除" + tmp);//设置显示文本
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override //设置确定按钮动作
            public void onClick(DialogInterface dialogInterface, int i) {
                data.removeCart_list_index(position);
                myAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "成功删除 " + tmp, Toast.LENGTH_SHORT).show();
                double sum=0;
                for(int j=1;j<data.getCart_list().size();j++){
                    sum+= data.getCart_list_index(j).getCnt()* data.getCart_list_index(j).getPrices();
                }
                all.setText("总价：￥ "+ String.valueOf(sum));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override //设置取消按钮动作
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "您选择了取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true); //允许取消
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        data=event.getData();
    }*/
    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void onMessageStickyEvent(MessageEvent event){
        data=event.getData();
        get_set_data();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

}
