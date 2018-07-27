package cn.chonor.project_1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static cn.chonor.project_1.R.styleable.View;

/**
 * Created by Chonor on 2017/11/17.
 */

public class Add_Role extends Activity {//Mark
    private static final int IMAGE = 10;
    private Data data=new Data();
    private LayoutParams para = null;
    private int height=0,width=0;
    private ImageView imageView=null;

    private TextInputLayout add_name=null;
    private TextInputLayout add_place=null;
    private NumberPicker add_dead=null;
    private NumberPicker add_both=null;
    private EditText add_info=null;
    private RadioButton add_man=null;
    private RadioButton add_woman=null;
    private int max_year;//最晚年份
    private int min_year;//最早年份，既然可以留0不能负数，那就是0
    private int choose_both;
    private int choose_dead;

    private View decorView;
    private float downX, downY;
    private float screenWidth, screenHeight;

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
        ////////////注册 Jy
        add_name=(TextInputLayout)findViewById(R.id.add_name);
        add_place=(TextInputLayout)findViewById(R.id.add_place);
        add_info=(EditText)findViewById(R.id.add_info);
        add_both=(NumberPicker)findViewById(R.id.add_both);
        add_dead=(NumberPicker)findViewById(R.id.add_dead);
        add_man=(RadioButton)findViewById(R.id.add_sex1);
        add_woman=(RadioButton)findViewById(R.id.add_sex2);

        max_year=2017;
        min_year=0;
        choose_both=0;
        choose_dead=0;
        add_both.setValue(choose_both);
        add_dead.setValue(choose_dead);//当前值
        add_both.setMinValue(min_year);
        add_dead.setMinValue(min_year);
        add_both.setMaxValue(max_year);
        add_dead.setMaxValue(max_year);

        //动画部分
        decorView = getWindow().getDecorView();//获得decorView
        DisplayMetrics metrics = new DisplayMetrics();//手机屏幕的宽度和高度，单位像素
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

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
                add_name=(TextInputLayout)findViewById(R.id.add_name);
                String Input_text=add_name.getEditText().toString();
                if(Input_text.equals("")){
                    Toast.makeText(getApplicationContext(),"姓名不可为空,请重新填写",Toast.LENGTH_LONG).show();
                }
                else{
                    data.setName(Input_text);
                }
            }
        });

        add_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_place=(TextInputLayout)findViewById(R.id.add_place);
                String Input_text=add_place.getEditText().toString();
                if(Input_text.equals("")){
                    Toast.makeText(getApplicationContext(),"籍贯不可为空,请重新填写",Toast.LENGTH_LONG).show();
                }
                else{
                    data.setPlace(Input_text);
                }
            }
        });

        add_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_info=(EditText)findViewById(R.id.add_info);
                String Input_text=add_info.getText().toString();
                data.setInfo(Input_text);
            }
        });

        add_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSex(1);
            }
        });
        add_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setSex(0);
            }
        });

        add_both.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                choose_both=i1;
            }
        });
        add_dead.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                choose_dead=i1;
            }
        });
        data.setBoth_and_Dead(choose_both,choose_dead);//看了下大概只能在这里设了，因为两个同时传入
        //关于性别，肯定是必填的，不处理
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){//按下
            downX = event.getX();
        }
        else if(event.getAction() == MotionEvent.ACTION_MOVE){//手指滑动，根布局跟着动
            float moveDistanceX = event.getX() - downX;//滑过的距离
            if(moveDistanceX > 0){//向右滑动
                decorView.setX(moveDistanceX); //设置界面的X到滑动到的位置
            }
            else if(moveDistanceX < 0){//向左滑动
                decorView.setX(moveDistanceX);
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP){//抬起手指，判断阈值，切换界面
            float moveDistanceX = event.getX() - downX;
            if(moveDistanceX > screenWidth / 3){//左滑超过1/3
                continueMove(moveDistanceX);
            }
            else if(moveDistanceX < -1*(screenWidth / 3) ){ //右滑超过1/3
                //保存数据
                //Toast.makeText(getApplicationContext(),"数据已保存",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"数据保存功能",Toast.LENGTH_LONG).show();
            }
            else{//往回滑动恢复初始状态
                ObjectAnimator.ofFloat(decorView, "X", moveDistanceX, 0).setDuration(300).start();
            }
        }
        return super.onTouchEvent(event);
    }

    private void continueMove(float moveDistanceX){//动画滑动处屏幕后再结束当前Activity
        ValueAnimator anim = ValueAnimator.ofFloat(moveDistanceX, screenWidth);
        anim.setDuration(1000); //一秒的时间结束
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (float) (animation.getAnimatedValue());//位移
                decorView.setX(x);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {//动画结束时结束当前Activity
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        });
    }

}
