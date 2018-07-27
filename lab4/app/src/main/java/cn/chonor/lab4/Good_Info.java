package cn.chonor.lab4;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Chonor on 2017/10/21.
 */

public class Good_Info extends AppCompatActivity {
    private int id;
    private Data data=new Data();
    private ImageView imageView;
    private ImageView back;
    private ImageView cart;
    private ImageView star;
    private TextView name;
    private TextView price;
    private TextView info;
    private ListView list;
    private ArrayList<Good> hit;
    private boolean flag=false;
    public static final String DYNAMICATION= "cn.chonor.lab4.dynamicreceiver";
    private DynamicReceiver dynamicReceiver=new DynamicReceiver();
    private IntentFilter dynamic_filter =new IntentFilter();
    private String names;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_info);
        init(); //初始化
        broad_init();
        get_set_data();//获取数据
        init_listener();//设置监听
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        get_set_data();//获取数据
        broad_init();
    }
    private void broad_init(){
        dynamic_filter.addAction(DYNAMICATION);
        registerReceiver(dynamicReceiver,dynamic_filter);
    }
    private void init(){ //初始化
        String []hits ={"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        hit=new ArrayList<>();
        for(int i=0;i<4;i++){//假装商品数据装进去
            Good good = new Good();
            good.setGoodName(hits[i]);
            hit.add(good);
        }
        imageView=(ImageView)findViewById(R.id.imageView);
        star = (ImageView)findViewById(R.id.imageView2);
        cart = (ImageView)findViewById(R.id.imageView3);
        back = (ImageView)findViewById(R.id.imageView4);
        name = (TextView)findViewById(R.id.info_name);
        price = (TextView)findViewById(R.id.info_price);
        info = (TextView)findViewById(R.id.info_info);
        list = (ListView)findViewById(R.id.myListView1);
    }
    private  void get_set_data(){
        Bundle extras_main = getIntent().getBundleExtra("mainActivity"); //获取数据  主界面
        Bundle extras_shop = getIntent().getBundleExtra("shoppingcart");//购物车
        if (extras_main != null||extras_shop != null) {
            if(extras_main != null) {  //主界面
                flag = true;            //设置flag
                id=extras_main.getInt("position"); //获得数据位置
                ArrayList<Good>tmp = extras_main.getParcelableArrayList("data"); //获得数据
                ArrayList<Good>tmp1 = extras_main.getParcelableArrayList("cart");
                data.setGood_list(tmp);//设置数据
                data.setCart_list(tmp1);
            }
            else{
                flag=false;//购物车//设置flag
                id=extras_shop.getInt("position");//获得数据位置
                ArrayList<Good>tmp = extras_shop.getParcelableArrayList("data");//获得数据
                ArrayList<Good>tmp1 = extras_shop.getParcelableArrayList("cart");
                data.setGood_list(tmp);//设置数据
                data.setCart_list(tmp1);
            }
            set_info();//设置界面
        }
    }
    private void set_info(){
        if(flag){//商品列表转跳时
            names=data.getGood_list_index(id).getGoodName();
            price.setText(data.getGood_list_index(id).getGoodPrice());
            if (data.getGood_list_index(id).getStar() == 1)
                star.setImageResource(R.mipmap.full_star);
            else star.setImageResource(R.mipmap.empty_star);
            info.setText(data.getGood_list_index(id).getGoodTypes() + " " + data.getGood_list_index(id).getGoodInfo());
        }
        else {//购物车列表转跳时
            names=data.getCart_list_index(id).getGoodName();
            price.setText(data.getCart_list_index(id).getGoodPrice());
            if (data.getCart_list_index(id).getStar() == 1)
                star.setImageResource(R.mipmap.full_star);
            else star.setImageResource(R.mipmap.empty_star);
            info.setText(data.getCart_list_index(id).getGoodTypes() + " " + data.getCart_list_index(id).getGoodInfo());
        }
        name.setText(names);
        imageView.setImageResource(data.ID[data.map.get(names)]);
        //设置下面4个items
        MyAdapter myAdapter=new MyAdapter(this,hit,1);
        list.setAdapter(myAdapter);
    }
    private void init_listener(){
        star.setOnClickListener(new View.OnClickListener() { //收藏按钮监听
            @Override
            public void onClick(View view) {
                if(flag) {//商品列表转跳时
                    if (data.getGood_list_index(id).getStar() == 1) {//判断是否收藏
                        star.setImageResource(R.mipmap.empty_star);
                        Toast.makeText(Good_Info.this,"成功将"+data.getGood_list_index(id).getGoodName()+"取消收藏",Toast.LENGTH_SHORT).show();
                    } else {
                        star.setImageResource(R.mipmap.full_star);
                        Toast.makeText(Good_Info.this,"成功将"+data.getGood_list_index(id).getGoodName()+"加入收藏",Toast.LENGTH_SHORT).show();
                    }
                    data.setGood_list_Stat(id); //同步购物车列表收藏
                    for(int i=0;i<data.getCart_list().size();i++){
                        if(data.getGood_list_index(id).getGoodName().equals(data.getCart_list_index(i).getGoodName())){
                            data.setCart_list_Stat(i);
                        }
                    }
                }else{//购物车列表转调试
                    if (data.getCart_list_index(id).getStar() == 1) {//判断是否收藏
                        star.setImageResource(R.mipmap.empty_star);
                        Toast.makeText(Good_Info.this,"成功将"+data.getCart_list_index(id).getGoodName()+"取消收藏",Toast.LENGTH_SHORT).show();
                    } else {
                        star.setImageResource(R.mipmap.full_star);
                        Toast.makeText(Good_Info.this,"成功将"+data.getCart_list_index(id).getGoodName()+"加入收藏",Toast.LENGTH_SHORT).show();
                    }
                    data.setCart_list_Stat(id); //同步商品列表收藏
                    for(int i=0;i<data.getGood_list().size();i++){
                        if(data.getCart_list_index(id).getGoodName().equals(data.getGood_list_index(i).getGoodName())){
                            data.setGood_list_Stat(i);
                        }
                    }
                }
            }
        });

        cart.setOnClickListener(new View.OnClickListener() { //购物车按钮
            @Override
            public void onClick(View view) {
                if(flag){
                    int incart= -1;  //确定购物车里有没有
                    for(int i=0;i<data.getCart_list().size();i++){
                        if(data.getGood_list_index(id).getGoodName().equals(data.getCart_list_index(i).getGoodName()))
                            incart=i;
                    }
                    if(incart== -1) //没有加入购物车
                        data.addCart_list(data.getGood_list_index(id));
                    else //有数量+1
                        data.setCart_list_Cnt(incart,data.getCart_list_index(incart).getCnt()+1);
                    Toast.makeText(Good_Info.this,"成功将"+data.getGood_list_index(id).getGoodName()+"加入购物车",Toast.LENGTH_SHORT).show();
                }
                else {
                    data.setCart_list_Cnt(id,data.getCart_list_index(id).getCnt()+1);
                    Toast.makeText(Good_Info.this,"成功将"+data.getCart_list_index(id).getGoodName()+"加入购物车",Toast.LENGTH_SHORT).show();
                }
                EventBus.getDefault().postSticky(new MessageEvent(data)); //发送数据
                Intent iBroad=new Intent(DYNAMICATION);//发送广播
                sendBroadcast(iBroad);
            }
        });

        back.setOnClickListener(new View.OnClickListener() { //返回按钮监听
            @Override
            public void onClick(View view) {  //数据返回
                if(flag){
                    Intent i = new Intent(Good_Info.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", data.getGood_list());
                    bundle.putParcelableArrayList("cart", data.getCart_list());
                    i.putExtra("goodinfo", bundle);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(Good_Info.this, Shoppingcart.class);
                    startActivity(i);
                }
            }
        });
    }
    public class DynamicReceiver extends BroadcastReceiver {
        public static final String DYNAMICATION= "cn.chonor.lab4.dynamicreceiver";
        public DynamicReceiver(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DYNAMICATION)){
                NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent1 =new Intent (context,Shoppingcart.class);//点击事件和传输
                PendingIntent pi = PendingIntent.getActivities(context, 0, new Intent[]{intent1}, PendingIntent.FLAG_CANCEL_CURRENT);
                //实例化NotificationCompat.Builde并设置相关属性
                Notification.Builder builder = new Notification.Builder(context)
                        //设置小图标
                        .setSmallIcon(data.ID[data.map.get(names)])
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),data.ID[data.map.get(names)]))
                        //设置通知标题
                        .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                        .setContentTitle("马上下单")
                        //设置通知内容
                        .setContentText(names+"已添加到购物车")
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setWhen(System.currentTimeMillis())
                        .setPriority(Notification.PRIORITY_HIGH);
                notifyManager.notify(1, builder.build());
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(dynamicReceiver);
    }
}
