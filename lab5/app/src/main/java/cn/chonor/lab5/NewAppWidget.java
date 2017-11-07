package cn.chonor.lab5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    private static final String STATICACTION = "cn.chonor.lab5.staticreceiver";
    private static Data data=new Data();
    private boolean flag=true;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        //appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them;
        for (int appWidgetId : appWidgetIds) { //
            if(flag){ //第一次更新
                Intent i = new Intent(context, MainActivity.class);//添加跳转事件
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                views.setTextViewText(R.id.appwidget_text, "当前没有任何消息"); //设置初始信息
                views.setImageViewResource(R.id.appwidget_image, R.mipmap.ic_launcher);
                views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);//设置点击事件
                ComponentName me = new ComponentName(context, NewAppWidget.class);
                AppWidgetManager.getInstance(context).updateAppWidget(me, views);//更新
            }else
            updateAppWidget(context,appWidgetManager,appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

        init(context);
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onDisabled(Context context) {
        init(context);
        // Enter relevant functionality for when the last widget is disabled
    }
    private void init(Context context){
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, "当前没有任何消息");
        views.setImageViewResource(R.id.appwidget_image, R.mipmap.ic_launcher);
        views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);
        ComponentName me = new ComponentName(context, NewAppWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(me, views);
    }
    @Override
    public void onReceive(Context context, Intent intent){
        final String action = intent.getAction();
        if(action.equals(STATICACTION)) {//判断是静态广播
            flag = false;
            Bundle extras = intent.getBundleExtra("mainActivity");
            int postiton = extras.getInt("position"); //获得数据位置
            Intent i = new Intent(context, Good_Info.class);//设置转转跳
            Bundle bundle = new Bundle();
            bundle.putInt("position", postiton);//设置转跳时传输数据
            bundle.putParcelableArrayList("data", data.getGood_list());
            bundle.putParcelableArrayList("cart", data.getCart_list());
            i.putExtra("mainActivity", bundle);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);//更新widget
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setTextViewText(R.id.appwidget_text, data.getGood_list_index(postiton).getGoodName() + "仅售" + data.getGood_list_index(postiton).getGoodPrice() + "!");
            views.setImageViewResource(R.id.appwidget_image, data.ID[data.map.get(data.getGood_list_index(postiton).getGoodName())]);
            views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);//设置点击事件
            ComponentName me = new ComponentName(context, NewAppWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(me, views);//更新
        }
        super.onReceive(context, intent);
    }
    static void upDataDynamic(Context context,PendingIntent pendingIntent, String names){
        Data data=new Data();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        if(names.equals("")){  //购物侧清空
            views.setTextViewText(R.id.appwidget_text, "当前没有任何消息"); //设置初始信息
            views.setImageViewResource(R.id.appwidget_image, R.mipmap.ic_launcher);
        }else { //动态广播更新
            views.setTextViewText(R.id.appwidget_text, names + "已添加到购物车");//更新信息
            views.setImageViewResource(R.id.appwidget_image, data.ID[data.map.get(names)]);
        }
        views.setOnClickPendingIntent(R.id.appwidget, pendingIntent);//设置点击事件
        ComponentName me = new ComponentName(context, NewAppWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(me, views);//更新
    }
}

