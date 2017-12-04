package cn.chonor.sen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chonor.sen.decode.DecodeThread;
import cn.chonor.sen.R;

public class MainActivity extends AppCompatActivity  {

    private double BatteryT;        //电池温度
    private TextView textView;
    private ImageView mResultImage;
    private TextView mResultText;
    private Button button;
    private Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册电池信息广播接收器
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        init();//界面按钮初始化
        Check();
    }
    private  void init(){
        textView=(TextView)findViewById(R.id.textView);
        button=(Button)findViewById(R.id.button2);
        button1=(Button)findViewById(R.id.button3);
        mResultImage = (ImageView) findViewById(R.id.result_image);
        mResultText = (TextView) findViewById(R.id.result_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Positioning.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startScan = new Intent(MainActivity.this,CaptureActivity.class);
                startActivity(startScan);
            }
        });
    }
    public void Check(){
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            String result = extras.getString("result");
            mResultText.setText(result);

            Bitmap barcode = null;
            byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                // Mutable copy:
                barcode = barcode.copy(Bitmap.Config.RGB_565, true);
            }
            mResultImage.setImageBitmap(barcode);
        }
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                BatteryT = intent.getIntExtra("temperature", 0);  //电池温度 单位是0.1度
                textView.setText("当前温度:" + String.valueOf(BatteryT/10);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatInfoReceiver);
    }

}