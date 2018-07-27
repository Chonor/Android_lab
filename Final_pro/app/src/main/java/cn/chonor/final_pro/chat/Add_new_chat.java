package cn.chonor.final_pro.chat;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import cn.chonor.final_pro.R;

/**
 * Created by ASUS on 2017/12/25.
 */

public class Add_new_chat  extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageView;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
    private final int CAMERA_CODE = 1;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.add_new_chat);

        int screen_width=this.getWindowManager().getDefaultDisplay().getWidth();
        screen_width*=0.9;
        int screen_height=this.getWindowManager().getDefaultDisplay().getHeight();
        screen_height*=0.4;
        EditText chat_content=(EditText) findViewById(R.id.chat_content);
        chat_content.setWidth(screen_width);
        chat_content.setHeight(screen_height);
        findViewById(R.id.choose_img).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);
        imageView=(ImageView) findViewById(R.id.add_chat_img);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.choose_img){
            final String[] items=new String[]{"拍摄","从相册选择"};
            AlertDialog.Builder talk1=new AlertDialog.Builder(this);
            talk1.setTitle("上传头像");
            talk1.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0) {
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,CAMERA_CODE);

                    }
                    else if(which==1){
                        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                        getAlbum.setType(IMAGE_UNSPECIFIED);
                        startActivityForResult(getAlbum, IMAGE_CODE);
                    }
                }
            });
            talk1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            talk1.show();
        }
        else if(v.getId()==R.id.submit){ //点击提交按钮

        }
        else if(v.getId()==R.id.back){
            Add_new_chat.this.finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Bitmap bm = null;
        // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        if (requestCode == IMAGE_CODE) {
            try {
                Uri originalUri = data.getData(); // 获得图片的uri
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bm, 100, 100));  //使用系统的一个工具类，参数列表为 Bitmap Width,Height  这里使用压缩后显示，否则在华为手机上ImageView 没有显示
                // 显得到bitmap图片
                // imageView.setImageBitmap(bm);
                String[] proj = { MediaStore.Images.Media.DATA };
                // 好像是android多媒体数据库的封装接口，具体的看Android文档
                Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                // 按我个人理解 这个是获得用户选择的图片的索引值
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                // 最后根据索引值获取图片路径
                String path = cursor.getString(column_index);
            } catch (IOException e) {
                Log.e("TAG-->Error", e.toString());
            }
            finally {
                return;
            }
        }
        else if(requestCode==CAMERA_CODE){
            Bundle bundle=data.getExtras();
            Bitmap bitmap=(Bitmap)bundle.get("data");
            imageView.setImageBitmap(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
