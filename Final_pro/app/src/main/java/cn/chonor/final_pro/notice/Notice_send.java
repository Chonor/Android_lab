package cn.chonor.final_pro.notice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.chonor.final_pro.R;

/**
 * Created by Jy on 2017/12/24.
 */

public class Notice_send extends AppCompatActivity {
    private EditText notice_send_title;
    private EditText notice_send_date;
    private EditText notice_send_content;
    private Button notice_send_send;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_send_activity);
        setTitle("发布通知");

        notice_send_title=(EditText)findViewById(R.id.notice_send_title);
        notice_send_date=(EditText)findViewById(R.id.notice_send_date);
        notice_send_content=(EditText)findViewById(R.id.notice_send_content);
        notice_send_send=(Button)findViewById(R.id.notice_send_send);

        notice_send_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send_title=notice_send_title.getText().toString();
                String send_date=notice_send_date.getText().toString();
                String send_content=notice_send_content.getText().toString();

                if(send_title.equals("")){
                    Toast.makeText(getApplicationContext(),"通知标题不可为空",Toast.LENGTH_SHORT).show();
                }
                else if(send_content.equals("")){
                    Toast.makeText(getApplicationContext(),"通知内容不可为空",Toast.LENGTH_SHORT).show();
                }
                else if(send_date.equals("")){
                    Toast.makeText(getApplicationContext(),"通知截止日期不可为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    //发布通知
                    Toast.makeText(getApplicationContext(),"发布通知成功",Toast.LENGTH_SHORT).show();
                    //加入到数据库里
                }
            }
        });

    }
}