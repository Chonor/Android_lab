package cn.chonor.project_1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static cn.chonor.project_1.R.id.info_date;
import static cn.chonor.project_1.R.id.info_sex;

/**
 * Created by Chonor on 2017/11/17.
 */

public class Info extends AppCompatActivity {
    private static final int IMAGE = 10;
    private Data data=new Data();
    private LayoutParams para = null;
    private int height=0,width=0;
    private ImageView imageView=null;

    private TextView info_name=null;
    private TextView info_sex=null;/*Jy*/
    private TextView info_date=null;
    private TextView info_place=null;
    private TextView info_other=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        Receive_Data();
        Init();
        Init_Listener();
    }
    private void Init(){
        imageView=(ImageView)findViewById(R.id.info_image);
        int i = View.MeasureSpec.makeMeasureSpec(0, 0);
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        imageView.measure(i,j);
        height=imageView.getMeasuredHeight();
        width=imageView.getMeasuredWidth();
        //////////
        //注册
        info_name=(TextView)findViewById(R.id.info_name);
        info_sex=(TextView)findViewById(R.id.info_sex);/*Jy*/
        info_date=(TextView)findViewById(R.id.info_date);
        info_place=(TextView)findViewById(R.id.info_place);
        info_other=(TextView)findViewById(R.id.info_other);

        imageView.setImageBitmap(data.getBitmap());
        ////////////
    }

    private void Init_Listener(){//点击事件监听


        ////////////////
        /*Jy*/
        info_name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final EditText input_text=new EditText(MainActivity.instance_tempthis);
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.instance_tempthis);
                alertDialogBuilder.setTitle("请输入名字：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(input_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String Input_text=input_text.getText().toString();
                                if(Input_text.equals("")){//不可为空
                                    Toast.makeText(getApplicationContext(),"姓名不可为空,修改失败",Toast.LENGTH_LONG).show();
                                    //Return_no_change();
                                }
                                else{
                                    data.setName(Input_text);
                                    //Return_change();
                                }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Return_no_change();
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                /*
                //下面这句删掉我用来调试的
                Return_no_change();
                */
                return true;
            }
        });

        info_sex.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final EditText input_text=new EditText(MainActivity.instance_tempthis);
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.instance_tempthis);
                alertDialogBuilder.setTitle("请输入性别：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(input_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String Input_text=input_text.getText().toString();
                                if(Input_text.equals("男")||Input_text.equals("女")){//不可为空&&不可为男女外的东西
                                    if(Input_text.equals("男"))data.setSex(1);
                                    else data.setSex(0);
                                    Return_change();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"输入错误,修改失败",Toast.LENGTH_LONG).show();
                                    Return_no_change();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Return_no_change();
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                return true;
            }
        });

        info_date.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //final EditText input_text=new EditText(MainActivity.instance_tempthis);
                final View view_birth_dead=LayoutInflater.from(MainActivity.instance_tempthis).inflate(R.layout.alertdialog_birth_dead,null);
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.instance_tempthis);
                alertDialogBuilder.setTitle("请输生卒：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(view_birth_dead)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final EditText birth = (EditText)view_birth_dead.findViewById(R.id.alertdialog_birth);
                                final EditText dead = (EditText)view_birth_dead.findViewById(R.id.alertdialog_dead);
                                String b=birth.getText().toString();
                                String d=dead.getText().toString();
                                int birth_date=Integer.parseInt(b);
                                int dead_date=Integer.parseInt(d);
                                data.setBoth_and_Dead(birth_date,dead_date);//在布局里限制了输入的只有正数
                                Return_change();
                                /*
                                String Input_text=input_text.getText().toString();
                                if(Input_text.equals("")){//不可为空&&格式【?】
                                    Toast.makeText(getApplicationContext(),"输入错误,修改失败",Toast.LENGTH_LONG).show();
                                    Return_no_change();
                                }
                                else{
                                    data.setBoth_and_Dead(birth,dead);//Mark
                                    Return_change();
                                }
                                */
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Return_no_change();
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                return true;
            }
        });

        info_place.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final EditText input_text=new EditText(MainActivity.instance_tempthis);
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.instance_tempthis);
                alertDialogBuilder.setTitle("请输籍贯：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(input_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String Input_text=input_text.getText().toString();
                                if(Input_text.equals("")){//不可为空&&格式【?】
                                    Toast.makeText(getApplicationContext(),"输入错误,修改失败",Toast.LENGTH_LONG).show();
                                    Return_no_change();
                                }
                                else{
                                    data.setPlace(Input_text);
                                    //Mark，修改data
                                    Return_change();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Return_no_change();
                            }
                        })

                        .setCancelable(true)
                        .create().show();
                return true;
            }
        });

        info_other.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final EditText input_text=new EditText(MainActivity.instance_tempthis);
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.instance_tempthis);
                alertDialogBuilder.setTitle("请输入人物历史介绍：")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(input_text)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String Input_text=input_text.getText().toString();
                                data.setInfo(Input_text);
                                /*
                                if(Input_text.equals("")){//不可为空&&格式【?】
                                    Toast.makeText(getApplicationContext(),"输入错误,修改失败",Toast.LENGTH_LONG).show();
                                    Return_no_change();
                                }
                                else{
                                    data.setInfo(Input_text);
                                    Return_change();
                                }
                                */
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Return_no_change();
                            }
                        })
                        .setCancelable(true)
                        .create().show();
                return true;
            }
        });


        ////////
    }

    private void Receive_Data(){
        Bundle extras = getIntent().getBundleExtra("mainActivity");
        if(extras!=null){
            data=extras.getParcelable("data");
            data.setBitmap(BitmapFactory.decodeResource(getResources(),data.getId()));
        }
    }
    private void Return_change(){//修改了信息调用这个
        Intent i = new Intent(Info.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_add",true);
        bundle.putParcelable("data",data);
        i.putExtras(bundle);
        setResult(RESULT_OK,i);
        finish();
    }
    private void Return_no_change(){//没有修改调用这个
        Intent i = new Intent(Info.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_add",false);
        i.putExtras(bundle);
        setResult(RESULT_OK,i);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE && resultCode == RESULT_OK){
            try { //此处提示需要捕获异常 所以加了
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap photo = BitmapFactory.decodeStream(inputStream); //使用输入流转化图片
                this.data.setBitmap(photo);
                imageView.setImageBitmap(photo);
                para = imageView.getLayoutParams();// 设置自动宽高
                para.height = height;
                para.width = width;
                imageView.setLayoutParams(para);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
