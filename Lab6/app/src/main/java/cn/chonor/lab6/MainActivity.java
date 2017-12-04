package cn.chonor.lab6;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String>Music_name=new ArrayList<>();
    private ArrayList<String>Music_id=new ArrayList<>();
    private ArrayList<String>Music_albumid=new ArrayList<>();
    private TextView status,begin,end;
    private Button play,stop,quit;
    private ImageView imageView,imageView1;
    private SeekBar seekBar;
    private MusicService.MyBinder myBinder;
    private ServiceConnection serviceConnection;
    private ObjectAnimator animator,animator1;
    private SimpleDateFormat time;
    private ListView listView;
    private int sta;
    private int pos;
    private static boolean PERMISSIONS=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(MainActivity.this);//动态权限获取
        if(PERMISSIONS){
            Init();
            Init_listener();
            Init_animator();
            Init_Connect();
            Init_Handler();
        }
    }

    /**
     * UI初始化
     */
    private void Init(){
        status=(TextView)findViewById(R.id.status);
        begin=(TextView)findViewById(R.id.begin);
        end=(TextView)findViewById(R.id.end);
        play=(Button)findViewById(R.id.Play);
        stop=(Button)findViewById(R.id.Stop);
        quit=(Button)findViewById(R.id.Quit);
        imageView=(ImageView)findViewById(R.id.imageView);
        imageView1=(ImageView)findViewById(R.id.imageView2);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        listView=(ListView)findViewById(R.id.list);
        time = new SimpleDateFormat("mm:ss");

        sta=0;
        pos=0;
    }

    /**
     * 点击事件初始化 sta为不同点击事件对应flag，其为Handle更新UI的依据
     */
    private void Init_listener(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override //拖动监听
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {//拖动中
                if(b){
                    try {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        data.writeInt(i);
                        myBinder.transact(105, data, reply, 0);//更新播放位置
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}//开始拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//拖动完毕
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//播放暂停切换
                if(status.getText().toString().equals("Playing"))sta=2;
                else sta=1;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sta=3;
            }
        });//停止
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sta=4;
            }
        });//退出
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//listview点击
                pos=i;sta=5;
            }
        });
    }

    /**
     * 建立Activity和service的连接
     */
    private void Init_Connect(){
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {//连接的建立
                myBinder = (MusicService.MyBinder) service;
                try {
                    Parcel data=Parcel.obtain();
                    Parcel reply=Parcel.obtain();
                    myBinder.transact(107,data,reply,0);//初始化
                    int len=reply.readInt();//返回时长
                    seekBar.setMax(len); // 设置最大时长
                    end.setText(time.format(len));//设置时间
                    reply.readStringList(Music_name);
                    reply.readStringList(Music_id);
                    reply.readStringList(Music_albumid);
                    Bitmap bm = getArtwork(MainActivity.this, Integer.valueOf(Music_id.get(0)),Integer.valueOf(Music_albumid.get(0)),true);
                    if(bm!=null)imageView1.setImageBitmap(bm);
                    listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_expandable_list_item_1,Music_name));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {//断开连接
                myBinder = null;
            }//连接断开
        };

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 图片转动设置
     */
    private void Init_animator(){
        animator = ObjectAnimator.ofFloat(imageView, "rotation", 0.0f, 360.0f);
        //animator =ObjectAnimator.ofFloat(imageView, "rotation", 0.0f);
        //第一个参数是控件，第二个是变化方式，第三个是可变长参数，第4个是变化角度
        animator.setDuration(10000); //设定转一圈的时间
        animator.setInterpolator(new LinearInterpolator());//定义动画的变化速率，这里是线性
        animator.setRepeatCount(Animation.INFINITE); //设定无限循环
        animator1=ObjectAnimator.ofFloat(imageView1,"rotation", 0.0f, 360.0f);
        animator1.setDuration(10000); //设定转一圈的时间
        animator1.setInterpolator(new LinearInterpolator());//定义动画的变化速率，这里是线性
        animator1.setRepeatCount(Animation.INFINITE); //设定无限循环
    }

    /**
     * 使用Handler更新UI
     */
    private void Init_Handler(){
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1 || msg.what == 2) {//播放or暂停
                    if (msg.what == 1) {//播放
                        play.setText("Paused");
                        status.setText("Playing");
                        if (animator.isPaused()) {
                            animator.resume();
                            animator1.resume();
                        }
                        else {
                            animator.start();
                            animator1.start();
                        }
                    } else {//暂停
                        play.setText("PLAY");
                        status.setText("Paused");
                        animator.pause();
                        animator1.pause();
                    }
                    try {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        myBinder.transact(101, data, reply, 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == 3) {//停止
                    seekBar.setProgress(0);
                    play.setText("PLAY");
                    status.setText("Stopped");
                    animator.setCurrentPlayTime(0);
                    animator.cancel();
                    animator1.setCurrentPlayTime(0);
                    animator1.cancel();
                    try {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        myBinder.transact(102, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == 4) {//退出
                    try {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        myBinder.transact(103, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    unbindService(serviceConnection);//停止服务时解除绑定
                    serviceConnection = null;
                    try {
                        MainActivity.this.finish();
                        System.exit(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (msg.what == 5) { //点击更换歌曲
                    seekBar.setProgress(0);  //类似执行停止
                    begin.setText(time.format(0));
                    play.setText("PLAY");
                    status.setText("");
                    animator.setCurrentPlayTime(0); //动画初始化
                    animator.cancel();
                    animator1.setCurrentPlayTime(0);
                    animator1.cancel();
                    //获取图片
                    Bitmap bm = getArtwork(MainActivity.this, Integer.valueOf(Music_id.get(pos)),Integer.valueOf(Music_albumid.get(pos)),true);
                    if(bm!=null)imageView1.setImageBitmap(bm);
                    try {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        data.writeInt(pos);
                        myBinder.transact(106, data, reply, 0);
                        int len = reply.readInt();
                        seekBar.setMax(len); // 设置最大时长
                        end.setText(time.format(len));//设置时间
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                sta = 0;
                try {//音乐进度更新
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    myBinder.transact(104, data, reply, 0);
                    int progress = reply.readInt();
                    seekBar.setProgress(progress);
                    begin.setText(time.format(progress));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        /**
         * 新建子线程定时调用Hander
         */
        Thread mThread = new Thread(){ //子线程更新
            @Override
            public void run(){
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.obtainMessage(sta).sendToTarget();
                }
            }
        };
        mThread.start();
    }

    /**
     * 动态获取访问SD卡权限
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);//直接检测是否否写的权限
        if (permission != PackageManager.PERMISSION_GRANTED) { //没有就去申请
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        else PERMISSIONS=true;
    }

    /**
     * 根据权限返回的结果处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode , String permissions[],int[] grantResults){
        if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
        }
        else{
            Toast.makeText(MainActivity.this,"没有权限程序退出运行",Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
        return;
    }

    /**
     * 实现后台
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    /**
     * 获取专辑图片
     * @param context
     * @param song_id     歌曲id
     * @param album_id    专辑ID
     * @param allowdefault
     * @return
     */
    public static Bitmap getArtwork(Context context, long song_id, long album_id,
                                    boolean allowdefault) {
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1);
                if (bm != null) {
                    return bm;
                }
            }
            if (allowdefault) {
                return getDefaultArtwork(context);
            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri, album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        if (bm == null && allowdefault) {
                            return getDefaultArtwork(context);
                        }
                    }
                } else if (allowdefault) {
                    bm = getDefaultArtwork(context);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }

    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
        Bitmap bm = null;
        byte [] art = null;
        String path = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }
        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri, albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }

    private static Bitmap getDefaultArtwork(Context context) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(
                context.getResources().openRawResource(R.mipmap.image), null, opts);
    }
    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;
}
