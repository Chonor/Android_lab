package cn.chonor.final_pro.login_register;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

import cn.chonor.final_pro.info.CircleImageView;
import cn.chonor.final_pro.info.ImageUtils;
import cn.chonor.final_pro.R;

/**
 * Created by Jy on 2017/12/23.
 */

public class RegisterActivity extends AppCompatActivity {
    private Button register_create_button;
    private CircleImageView register_img;
    private EditText register_real_name;
    private EditText register_sid;
    private EditText register_department;
    private EditText register_name;
    private RadioGroup register_sex;
    private RadioButton register_male;
    private RadioButton register_female;
    private EditText register_first_password;
    private EditText register_second_password;
    private RadioGroup register_type;
    private RadioButton register_student;
    private RadioButton register_teacher;
    private int sex;
    private int choose_type;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static final String TAG = "Register_Img";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        setTitle("注册账户");

        register_create_button=(Button)findViewById(R.id.register_create_button);
        register_img=(CircleImageView)findViewById(R.id.register_img);
        register_real_name=(EditText)findViewById(R.id.register_real_name);
        register_sid=(EditText)findViewById(R.id.register_sid);
        register_department=(EditText)findViewById(R.id.register_department);
        register_name=(EditText)findViewById(R.id.register_name);
        register_sex=(RadioGroup)findViewById(R.id.register_sex);
        register_male=(RadioButton)findViewById(R.id.register_male);
        register_female=(RadioButton)findViewById(R.id.register_female);
        register_first_password=(EditText)findViewById(R.id.register_first_password);
        register_second_password=(EditText)findViewById(R.id.register_second_password);
        register_type=(RadioGroup)findViewById(R.id.register_type);
        register_student=(RadioButton)findViewById(R.id.register_student);
        register_teacher=(RadioButton)findViewById(R.id.register_teacher);

        register_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoosePicDialog();
            }
        });

        register_create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"create",Toast.LENGTH_SHORT).show();
                register_new_account();
            }
        });

    }
    /**
     * 显示设置头像的对话框
     */
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片", "拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        //Log.d(TAG,"takePicture_click");
                        takePicture();
                        break;
                }
            }
        });
        final AlertDialog dialog=builder.create();
        //尝试美化，透明
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.9f;
        window.setAttributes(lp);
        dialog.show();
    }

    /**
     * 拍照
     */
    private void takePicture() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            // 需要申请动态权限
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
      //  Log.d(TAG,"takePicture_permission_finish");
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File file = new File(Environment.getExternalStorageDirectory(), "image.jpg");
        File file = new File(Environment.getExternalStorageDirectory(), "IMG_20171224_081800.jpg");
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= 24) {
            openCameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            tempUri = FileProvider.getUriForFile(RegisterActivity.this, "com.example.jy.project_final.fileProvider", file);
        } else {
            //tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "image.jpg"));
            tempUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "IMG_20171224_081800.jpg"));
        }
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换

        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 150);// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.d(TAG,"setImageToView:"+photo);
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            register_img.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    /**
     * 接口，上传至数据库
     * @param bitmap
     */
    private void uploadPic(Bitmap bitmap) {
        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
            Log.d(TAG,"imagePath:"+imagePath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 注册账户
     * 补充创建用户操作。
     */
    private void register_new_account(){
        String real_name=register_real_name.getText().toString();
        String sid=register_sid.getText().toString();
        String department=register_department.getText().toString();
        String name=register_name.getText().toString();

        String first_pass=register_first_password.getText().toString();
        String second_pass=register_second_password.getText().toString();

        sex=register_sex.getCheckedRadioButtonId();
        register_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup,int i) {
                if(register_male.getId()==i) sex=0;
                else sex=1;
            }
        });

        choose_type=register_type.getCheckedRadioButtonId();
        register_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup,int i) {
                if(register_student.getId()==i) choose_type=0;
                else choose_type=1;
            }
        });

        if(check_enable(real_name,sid,name,first_pass,second_pass,choose_type)){
            //创建用户
            //返回登录界面
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        }
    }

    /**
     * 检查是否满足注册要求
     * 需要补充的是:检查sid是否已经存在于数据库内，如果是，那么不允许重复注册
     * 此外，是否要考虑忘记密码功能
     * 有时间会补充实时提醒，而不是toast
     * @param real_name
     * @param sid
     * @param name
     * @param first_pass
     * @param second_pass
     * @param choose_type
     * @return
     */
    private boolean check_enable(String real_name,String sid,String name,String first_pass,String second_pass,int choose_type){
        if(real_name.equals("")){
            Toast.makeText(getApplicationContext(),"必须填写真实姓名，请补充",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(sid.equals("")){
            Toast.makeText(getApplicationContext(),"必须填写学号，请补充",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(name.equals("")){
            Toast.makeText(getApplicationContext(),"必须填写昵称，请补充",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(first_pass.equals("")){
            Toast.makeText(getApplicationContext(),"必须填写密码，请补充",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!first_pass.equals(second_pass)){
            Toast.makeText(getApplicationContext(),"密码不一致，请重新填写",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(choose_type==-1){
            Toast.makeText(getApplicationContext(),"必须选择用户类型，请补充",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(check_ifExist(sid)){
            Toast.makeText(getApplicationContext(),"该学号已注册",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 判断sid是否已经存在于数据库内
     * @param sid 学号：True表示已存在
     * @return
     */
    private boolean check_ifExist(String sid){
        return false;
    }

}
