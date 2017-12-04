package cn.chonor.project_1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {
    //DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private static int ADD=0;
    private static int INFO=0;
    ArrayList<Data>datas=new ArrayList<>();
    private RecyclerView mRecyclerView = null;
    private CommonAdapter commonAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton floatingActionButton=null;
    private int click_position=-1;
    public static MainActivity instance_tempthis;//Jy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        //if(!databaseHelper.tableIsExist()){//这个数据库先停用
        Data_IN();//加点数据
       // }
        //databaseHelper.getWritableDatabase();
        ListView_display();
        Init_Listener();

        instance_tempthis=this;//Jy
    }

    private void Init(){
        mRecyclerView = (RecyclerView) findViewById(R.id.mainrecyclerview); //初始化RecyclerView
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_add); //初始化按钮
    }
    private void Init_Listener(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {//增加界面
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Add_Role.class);
                startActivityForResult(i,ADD);
            }
        });

        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                if(position>=0) {
                    click_position=position;
                    Intent i = new Intent(MainActivity.this, Info.class);
                    Bundle bundle = new Bundle();
                    Data tmp=datas.get(position);
                    bundle.putParcelable("data", tmp); //传输一个data类进去
                    i.putExtra("mainActivity", bundle);
                    startActivityForResult(i,INFO);
                }
            }

            @Override
            public void onLongClick(final int position) {
                //删除
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.instance_tempthis);
                alertDialogBuilder.setTitle("删除人物")
                        .setMessage("确定删除人物？")
                        .setIcon(android.R.drawable.ic_dialog_info);
                alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                alertDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        datas.remove(position);//....不是这一句就搞定了吗??????
                    }
                });
                alertDialogBuilder.setCancelable(true);//误点击可取消
                alertDialogBuilder.create().show();
            }
        });
    }
    private void ListView_display(){
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        commonAdapter = new CommonAdapter(MainActivity.this, R.layout.main_items, datas);
        mRecyclerView.setAdapter(commonAdapter);//填充数据
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent); //数据回传
        if (resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            if(extras!=null) {
                if (requestCode == ADD) { //添加界面
                    boolean is_add = extras.getBoolean("is_add");
                    if (is_add) {
                        Data tmp = new Data();
                        extras.putParcelable("new_add", tmp);
                        datas.add(tmp);
                        commonAdapter.notifyDataSetChanged();
                    }
                } else if (requestCode == INFO) {//信息界面
                    boolean is_change = extras.getBoolean("is_change");
                    if (is_change) {//更新数据
                        Data tmp = new Data();
                        extras.putParcelable("is_change", tmp);
                        datas.remove(click_position);
                        commonAdapter.notifyDataSetChanged();
                        datas.add(click_position, tmp);
                        commonAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void Data_IN(){//准备的一点数据
        String namse[]={"刘备","关羽","张飞","赵云","诸葛亮","马超"};
        String places[]={"幽州涿郡涿(河北保定市涿州)","司隶河东郡解(山西运城市临猗县西南)","幽州涿郡(河北保定市涿州)","冀州常山国真定(河北石家庄市正定县南)","徐州琅邪国阳都(山东临沂市沂南县南)","司隶扶风茂陵(陕西咸阳市兴平东)"};
        String Infos[]={"蜀汉的开国皇帝，相传是汉景帝之子中山靖王刘胜的后代。刘备少年丧父，与母亲贩鞋织草席为生。","前将军。本字长生，亡命奔涿郡。与张飞追随刘备征战，当刘备为平原相时，他们俩为别部司马。二人与刘备寝则同床，恩若兄弟。","车骑将军、司隶校尉。年少时与关羽投靠刘备，三人恩如兄弟。刘备被公孙瓒表为平原相后刘备以其为別部司马。","以英勇善战、一身是胆著称。初为本郡所举，将义从吏兵诣公孙瓒。时袁绍称冀州牧，瓒深忧州人之从绍也，善云来附，遂与瓒征讨。","政治家、军事家，被誉为“千古良相”的典范。父母早亡，由叔父玄抚养长大，后因徐州之乱，避乱荆州，潜心向学，淡泊明志。","马腾遣马超随钟繇讨郭援于平阳，马超为飞矢所中，乃以布带裹好受伤的小腿继续战斗，终破袁军。"};
        int sexs[]={1,1,1,1,1,1};
        int boths[]={161,0,0,0,181,176};
        int dead[]={223,219,221,229,234,222};
        int image_id[]={R.mipmap.liubei,R.mipmap.guanyu,R.mipmap.zhangfei,R.mipmap.zhaoyun,R.mipmap.zhuge,R.mipmap.machao};
        for(int i=0;i<6;i++){
            Bitmap bitmap=BitmapFactory.decodeResource(getResources(),image_id[i]);
            Data tmp=new Data(namse[i],places[i],Infos[i],sexs[i],boths[i],dead[i], bitmap);
            tmp.setId(image_id[i]);
            datas.add(tmp);
            //databaseHelper.Insert(datas.get(i));
        }
    }



}
