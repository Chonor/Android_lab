package cn.chonor.project_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Chonor on 2017/11/17.
 */

public class Add_Role extends AppCompatActivity {
    private static final int IMAGE = 10;
    private Data data=new Data();
    private LayoutParams para = null;
    private int height=0,width=0;
    private ImageView imageView=null;

    private TextInputLayout add_name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        Init();
        Init_Listener();
    }
    private void Init(){
        imageView=(ImageView)findViewById(R.id.add_image);
        int i = View.MeasureSpec.makeMeasureSpec(0, 0);
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        imageView.measure(i,j);
        height=imageView.getMeasuredHeight();
        width=imageView.getMeasuredWidth();
        //////////
        add_name=(TextInputLayout)findViewById(R.id.add_name);
        //注册
        ////////////
    }

    private void Init_Listener(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE);
            }
        });

        ///////////////////////
        add_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //下面这句调试删掉他
                Returns_null();
            }
        });

        ///////////////
    }

    private void Returns_Add_new(){//如果加了数据调用这个返回
        Intent i = new Intent(Add_Role.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_add",true);
        bundle.putParcelable("data",data);
        i.putExtras(bundle);
        setResult(RESULT_OK,i);
        finish();
    }
    private void Returns_null(){//不添加调用这个返回
        Intent i = new Intent(Add_Role.this, MainActivity.class);
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
