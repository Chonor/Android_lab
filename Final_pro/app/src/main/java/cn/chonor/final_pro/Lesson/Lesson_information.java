package cn.chonor.final_pro.Lesson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.chonor.final_pro.R;
import cn.chonor.final_pro.notice.Notice_send;

/**
 * Created by Jy on 2017/12/24.
 */

/**
 * 课程列表
 */
public class Lesson_information extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_information_activity);

        Intent intent=getIntent();
        String lesson_name = intent.getStringExtra("lesson_name");
        String lesson_info = intent.getStringExtra("lesson_info");
        String lesson_teacher = intent.getStringExtra("lesson_teacher");
        setTitle(lesson_name);

        init(lesson_name,lesson_info,lesson_teacher);

        final Button button=(Button)findViewById(R.id.lesson_infor_checknotice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type=0;
                /*
                //从数据库里查看用户类型
                type=;
                 */
                if(type==0){
                    //这里只能通过数据库来进行跳转了，比如通知的id
                }
                else {
                    String a="发布课程信息";
                    button.setText(a.toCharArray(),0,a.length());
                    //跳转到发布通知页面
                    startActivity(new Intent(Lesson_information.this,Notice_send.class));
                }
            }
        });

    }

    /**
     * 初始化页面信息，虽然别的用了intent传了过来，
     * 可是开课单位和简介还是要查数据库才可以完成初始化
     * @param lesson_name 课程名字
     * @param lesson_info 课程时间地点
     * @param lesson_teacher 任课老师
     */
    private void init(String lesson_name,String lesson_info,String lesson_teacher){
        ((TextView)findViewById(R.id.lesson_infor_name)).setText(lesson_name.toCharArray(),0,lesson_name.length());
        String info="课程安排："+lesson_info;
        String teacher="任课老师："+lesson_teacher;
        ((TextView)findViewById(R.id.lesson_infor_info)).setText(info.toCharArray(),0,info.length());
        ((TextView)findViewById(R.id.lesson_infor_teacher)).setText(teacher.toCharArray(),0,teacher.length());


        /*
        //从数据库里查课程简介、开课单位
        String introduction=;
        String danwei=;
        ((TextView)findViewById(R.id.lesson_infor_introduction)).setText(introduction.toCharArray(),0,introduction.length());
        ((TextView)findViewById(R.id.lesson_infor_kaikedanwei)).setText(danwei.toCharArray(),0,danwei.length());
         */


    }

}