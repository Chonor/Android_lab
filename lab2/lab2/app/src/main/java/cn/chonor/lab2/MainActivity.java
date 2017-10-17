package cn.chonor.lab2;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.view.ViewGroup.LayoutParams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA = 1;
    private static final int IMAGE = 2;
    private String rb_string="学生";  //初始化为默认选项学生
    private String rb_string_hit="学号";
    int height=0;
    int width=0;
    private ImageView mImage=null;
    private Button button_login=null;
    private Button button_init=null;
    private RadioGroup mRB=null;
    private RadioButton rb_student=null;
    private RadioButton rb_teacher=null;
    private EditText et_id=null;
    private EditText et_password=null;
    private TextInputLayout til_id =null;
    private TextInputLayout til_password=null;
    private AlertDialog.Builder builder=null;
    private LayoutParams para = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        alertdialog_build();
        listener_init();
    }
    public void init(){
        mImage = (ImageView) findViewById(R.id.imageView);
        button_login = (Button) findViewById(R.id.button);
        button_init = (Button) findViewById(R.id.button2);
        mRB = (RadioGroup) findViewById(R.id.radioGroup);
        rb_student = (RadioButton) findViewById(R.id.radioButton1);
        rb_teacher = (RadioButton) findViewById(R.id.radioButton2);
        et_id = (EditText) findViewById(R.id.textid);
        et_password = (EditText) findViewById(R.id.textpassword);
        til_id = (TextInputLayout) findViewById(R.id.textInputLayout);
        til_password = (TextInputLayout) findViewById(R.id.textInputLayout1);
        builder=new AlertDialog.Builder(this);
        //测量图片大小//额外
        int i = View.MeasureSpec.makeMeasureSpec(0, 0);
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        mImage.measure(i,j);
        height=mImage.getMeasuredHeight();
        width=mImage.getMeasuredWidth();
    }
    public void alertdialog_build(){
        //alertdialog 初始化
        builder.setTitle("上传头像");
        final String[] Items={"拍摄","从相册选择"};
        builder.setItems(Items,new DialogInterface.OnClickListener(){
            @Override   //使用setItems构建选择列表 并增加点击检测
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "您选择了["+Items[i]+"]", Toast.LENGTH_SHORT).show();
                if(i==0){//额外 打开相机
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA);
                }else {//额外 打开相册
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override //设置取消按钮动作
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"您选择了[取消]", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true); //允许取消
    }

    public void listener_init(){
        //图片按钮监听
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=builder.create(); //完成创建AlertDialog并显示
                dialog.show();
            }
        });
        //radio 监听
        mRB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_student.getId() == checkedId) { //保存当前选择
                    rb_string = rb_student.getText().toString();
                    rb_string_hit="学号";
                }
                if (rb_teacher.getId() == checkedId) {
                    rb_string = rb_teacher.getText().toString();
                    rb_string_hit="教职工号";
                }
                til_id.setHint("请输入"+rb_string_hit);
                if(til_id.isErrorEnabled())til_id.setError(rb_string_hit+"不能为空");
                Snackbar.make(button_init, "您选择了"+rb_string, Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });
        //输入框输入 监听
        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) { //额外//输入完毕自主判断
                if (editable.length() == 0) {
                    til_id.setErrorEnabled(true);
                    til_id.setError(rb_string_hit+"不能为空");
                } else {
                    til_id.setErrorEnabled(false);
                }
            }
        });
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {//额外
                if (editable.length() == 0) {
                    til_password.setErrorEnabled(true);
                    til_password.setError("密码不能为空");
                } else {
                    til_password.setErrorEnabled(false);
                }
            }
        });
        //登录按钮监听
        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(et_id.getText().toString().length()==0){  //先判断 学号
                    til_id.setErrorEnabled(true);
                    til_id.setError(rb_string_hit+"不能为空");
                }
                else if(et_password.getText().toString().length()==0) { //再判断 密码
                    til_password.setErrorEnabled(true);
                    til_password.setError("密码不能为空");
                }
                else if(et_id.getText().toString().equals("123456") && et_password.getText().toString().equals("6666")) { //如果都不为空 判断是否中输入正确
                    Snackbar.make(button_init, "登录成功", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .show();
                    if(rb_string.equals("学生")){
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask(){
                            @Override
                            public void run() {
                                Intent i = new Intent(MainActivity.this ,AfterLogin.class);
                                startActivity(i);
                            }
                        },2000);//延时2s执行
                    }
                }
                else if(et_id.getText().toString().length()!=0 && et_password.getText().toString().length()!=0) { //输入不正确
                    Snackbar.make(button_init, rb_string_hit+"或密码错误", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .show();
                }
            }
        });
        //注册按钮监听
        button_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(button_init, rb_string + "注册功能尚未启用", Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });
    }
    //额外
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mImage.setImageBitmap(photo); //设置图片
            para = mImage.getLayoutParams();    // 设置自动宽高
            para.height = height;
            para.width = width;
            mImage.setLayoutParams(para);

        }else if(requestCode == IMAGE && resultCode == RESULT_OK){
            try { //此处提示需要捕获异常 所以加了
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap photo = BitmapFactory.decodeStream(inputStream); //使用输入流转化图片
                mImage.setImageBitmap(photo);
                para = mImage.getLayoutParams();// 设置自动宽高
                para.height = height;
                para.width = width;
                mImage.setLayoutParams(para);
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
