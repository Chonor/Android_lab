package cn.chonor.final_pro.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import cn.chonor.final_pro.R;

/**
 * Created by Jy on 2017/12/25.
 */

/**
 * 通知详细信息，这里直接用了intent传过来的参数，没有需要调用数据库的
 */
public class Notice_information extends AppCompatActivity {
    private TextView notice_infor_title;
    private TextView notice_infor_date;
    private TextView notice_infor_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_information_activity);
        setTitle("通知");


        Intent intent=getIntent();
        String notice_title=intent.getStringExtra("notice_title");
        String notice_date=intent.getStringExtra("notice_date");
        String notice_content=intent.getStringExtra("notice_content");

        notice_infor_title=(TextView)findViewById(R.id.notice_infor_title);
        notice_infor_date=(TextView)findViewById(R.id.notice_infor_date);
        notice_infor_content=(TextView)findViewById(R.id.notice_infor_content);


        notice_infor_title.setText(notice_title.toCharArray(),0,notice_title.length());
        notice_infor_content.setText(notice_content.toCharArray(),0,notice_content.length());
        notice_infor_date.setText(notice_date.toCharArray(),0,notice_date.length());
    }
}
