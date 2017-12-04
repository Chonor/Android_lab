package cn.chonor.project_1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class Info extends AppCompatActivity {
    private static final int IMAGE = 10;
    private Data data=new Data();
    private LayoutParams para = null;
    private int height=0,width=0;
    private ImageView imageView=null;

    private TextView info_name=null;
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
        imageView.setImageBitmap(data.getBitmap());
        ////////////
    }

    private void Init_Listener(){//点击事件监听
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //这里改为弹出alertdialog询问
                //记得转移到alertdialog确定按钮
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE);
            }
        });

        ////////////////
        info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //这里是修改
                //下面这句删掉我用来调试的
                Return_no_change();
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
