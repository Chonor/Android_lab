package cn.chonor.lab4;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton=null;
    private Data data=new Data();
    private RecyclerView mRecyclerView = null;
    private CommonAdapter commonAdapter;
    private ScaleInAnimationAdapter animationAdapter;
    private Button button;
    private Boolean flag;
    protected RecyclerView.LayoutManager mLayoutManager;

    private static final String STATICACTION = "cn.chonor.lab4.staticreceiver";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        init_listener();
        broad_init();
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        back_data();
    }

    private void back_data(){
        Bundle extras_info = getIntent().getBundleExtra("goodinfo");
        Bundle extras_shop = getIntent().getBundleExtra("shoppingcart");//购物车
        if (extras_info != null||extras_shop != null) {
            if(extras_info != null) {  //主界面
                ArrayList<Good>tmp = extras_info.getParcelableArrayList("data"); //获得数据
                ArrayList<Good>tmp1 = extras_info.getParcelableArrayList("cart");
                data.setGood_list(tmp);//设置数据
                data.setCart_list(tmp1);
            }
            else{
                ArrayList<Good>tmp = extras_shop.getParcelableArrayList("data");//获得数据
                ArrayList<Good>tmp1 = extras_shop.getParcelableArrayList("cart");
                data.setGood_list(tmp);//设置数据
                data.setCart_list(tmp1);
            }
        }
    }
    private void init() {
        flag=true;
        button = (Button) findViewById(R.id.button);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton); //初始化按钮
        mRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView); //初始化RecyclerView

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        commonAdapter = new CommonAdapter(MainActivity.this, R.layout.items, data.getGood_list(), flag);
        animationAdapter = new ScaleInAnimationAdapter(commonAdapter);

        animationAdapter.setDuration(1000); //设置开始动画
        mRecyclerView.setAdapter(animationAdapter);//填充数据
        mRecyclerView.setItemAnimator(new OvershootInLeftAnimator());
        mRecyclerView.getItemAnimator().setRemoveDuration(300); //设置移除延时

    }
    private void init_listener(){
        Adapter_listener();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override //跳转至 购物车界面
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Shoppingcart.class);
                Bundle bundle = new Bundle();//传输数据
                bundle.putParcelableArrayList("data",data.getGood_list());
                bundle.putParcelableArrayList("cart",data.getCart_list());
                i.putExtra("mainActivity",bundle);
                startActivity(i);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){
                    flag=false;
                }else{
                    flag=true;
                }
                change();
            }
        });
    }
    private void Adapter_listener(){
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener(){
            @Override
            public void onClick(int position){
                if(position >= 0) {
                    Intent i = new Intent(MainActivity.this, Good_Info.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", position);
                    bundle.putParcelableArrayList("data", data.getGood_list());
                    bundle.putParcelableArrayList("cart", data.getCart_list());
                    i.putExtra("mainActivity", bundle);
                    startActivity(i);
                }
            }
            @Override
            public void onLongClick(int position) {
                if (position >= 0) {
                    data.removeGood_list_index(position);
                    animationAdapter.notifyItemRemoved(position);
                    mRecyclerView.setItemViewCacheSize(data.getGood_list().size());
                    Toast.makeText(MainActivity.this, "移除了第"+String.valueOf(position)+"件商品", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void change(){
        int scrollPosition = 0;
        // If a layout manager has already been set, get current scroll position.
        if (mRecyclerView.getLayoutManager() != null) {
            scrollPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager())
                    .findFirstCompletelyVisibleItemPosition();
        }//布局切换
        if(flag) {
            mLayoutManager = new LinearLayoutManager(this);
            commonAdapter = new CommonAdapter(MainActivity.this, R.layout.items, data.getGood_list(), flag);
        }else{
            mLayoutManager = new GridLayoutManager(this, 2);
            commonAdapter = new CommonAdapter(MainActivity.this, R.layout.item2, data.getGood_list(), flag);
        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        animationAdapter = new ScaleInAnimationAdapter(commonAdapter);
        mRecyclerView.setAdapter(animationAdapter);//填充数据
        Adapter_listener();//建立监听
        mRecyclerView.scrollToPosition(scrollPosition);
    }
    private void broad_init(){
        Random random=new Random();
        int i=random.nextInt(10);
        Intent iBroad=new Intent(STATICACTION);
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        iBroad.putExtra("mainActivity",bundle);
        sendBroadcast(iBroad);
    }

    public static class StaticReceiver extends BroadcastReceiver {
        public StaticReceiver(){}
        @Override		//静态广播接收器执行的方法
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(STATICACTION)) {
                Bundle extras = intent.getBundleExtra("mainActivity");
                Data data=new Data();
                int id=extras.getInt("position"); //获得数据位置
                Bitmap bm=BitmapFactory.decodeResource(context.getResources(),data.ID[id]);
                String name=data.getGood_list_index(id).getGoodName();
                String price=data.getGood_list_index(id).getGoodPrice();

                NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent1 =new Intent (context,Good_Info.class); //点击事件和传输
                Bundle bundle = new Bundle();
                bundle.putInt("position", id);
                bundle.putParcelableArrayList("data",data.getGood_list());
                bundle.putParcelableArrayList("cart", data.getCart_list());
                intent1.putExtra("mainActivity", bundle);
                PendingIntent pi = PendingIntent.getActivities(context, 0, new Intent[]{intent1}, PendingIntent.FLAG_CANCEL_CURRENT);

                //实例化NotificationCompat.Builde并设置相关属性
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle()
                        .setBigContentTitle("新商品热卖")
                        .addLine(name+"仅售"+price+"!")
                        .addLine(data.getGood_list_index(id).getGoodTypes() + " " + data.getGood_list_index(id).getGoodInfo());

                Notification.Builder builder = new Notification.Builder(context)
                        //设置小图标
                        .setSmallIcon(data.ID[id])
                        .setLargeIcon(bm)
                        //设置通知标题
                        .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                        .setContentTitle("新商品热卖")
                        //设置通知内容
                        .setContentText(name+"仅售"+price+"!")
                        .setStyle(inboxStyle)
                        .setContentIntent(pi)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setWhen(System.currentTimeMillis())
                        .setPriority(Notification.PRIORITY_HIGH);
                //.setStyle(new Notification.BigPictureStyle().bigLargeIcon(bm).setBigContentTitle(name+"仅售"+price+"!").setSummaryText(data.getCart_list_index(id).getGoodTypes() + " " + data.getCart_list_index(id).getGoodInfo()));

                notifyManager.notify(0, builder.build());
            }
        }
    }
}
