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
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
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


public class MainActivity extends AppCompatActivity {
    private static final int CAMERA = 1;
    private static final int IMAGE = 2;
    String rb_string="";
    int height=0;
    int width=0;
    ImageView mImage=null;
    Button button_login=null;
    Button button_init=null;
    RadioGroup mRB=null;
    RadioButton rb_student=null;
    RadioButton rb_teacher=null;
    EditText et_id=null;
    EditText et_password=null;
    TextInputLayout til_id =null;
    TextInputLayout til_password=null;
    AlertDialog.Builder builder=null;
    LayoutParams para = null;
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
        rb_student = (RadioButton) findViewById(R.id.radioButton2);
        rb_teacher = (RadioButton) findViewById(R.id.radioButton1);
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
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "您选择了"+Items[i], Toast.LENGTH_SHORT).show();
                if(i==0){//额外
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA);
                }else {//额外
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(),"您选择了取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true);
    }

    public void listener_init(){
        //图片按钮监听
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        //radio 监听
        mRB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rb_student.getId() == checkedId) { //保存当前选择
                    rb_string = rb_student.getText().toString();
                }
                if (rb_teacher.getId() == checkedId) {
                    rb_string = rb_teacher.getText().toString();
                }
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
                til_id.setHint("请输入学号");
                til_id.setHintEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable editable) { //额外
                if (editable.length() == 0) {
                    til_id.setError("学号不能为空");
                    til_id.setErrorEnabled(true);
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
                til_password.setHint("请输入密码");
                til_password.setHintEnabled(true);
            }
            @Override
            public void afterTextChanged(Editable editable) {//额外
                if (editable.length() == 0) {
                    til_password.setError("密码不能为空");
                    til_password.setErrorEnabled(true);
                } else {
                    til_password.setErrorEnabled(false);
                }
            }
        });
        //登录按钮监听
        button_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),height+"px", Toast.LENGTH_SHORT).show();
                if(et_id.getText().toString().equals("123456")&&et_password.getText().toString().equals("6666")){
                    Snackbar.make(button_init, "登录成功", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(),"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .show();
                }else{
                    if(et_id.getText().toString().length()==0){
                        til_id.setError("学号不能为空");
                        til_id.setErrorEnabled(true);
                    }
                    if(et_password.getText().toString().length()==0) {
                        til_password.setError("密码不能为空");
                        til_password.setErrorEnabled(true);
                    }
                    Snackbar.make(button_init, "学号或密码错误", Snackbar.LENGTH_SHORT)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Toast.makeText(getApplicationContext(),"Snackbar的确定按钮被点击了",Toast.LENGTH_SHORT).show();
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
            mImage.setImageBitmap(photo);
            para = mImage.getLayoutParams();
            para.height = height;
            para.width = width;
            mImage.setLayoutParams(para);

        }else if(requestCode == IMAGE && resultCode == RESULT_OK){
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                mImage.setImageBitmap(photo);
                para = mImage.getLayoutParams();
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
