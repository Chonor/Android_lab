package cn.chonor.final_pro.info;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.chonor.final_pro.R;

/**
 * Created by Jy on 2017/12/24.
 */

/**
 * 个人编辑信息界面
 */
public class Edit_information extends AppCompatActivity {
    private CircleImageView edit_img;
    private EditText edit_name;
    private EditText edit_department;
    private RadioGroup edit_sex;
    private RadioButton edit_male;
    private RadioButton edit_female;
    private RadioButton edit_secret;
    private EditText edit_introduction;
    private Button edit_save;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        setTitle("修改个人信息");

        edit_img=(CircleImageView)findViewById(R.id.edit_img);
        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_department=(EditText)findViewById(R.id.edit_department);
        edit_sex=(RadioGroup)findViewById(R.id.edit_sex);
        edit_introduction=(EditText)findViewById(R.id.edit_introduction);
        edit_save=(Button)findViewById(R.id.edit_save);
        edit_male=(RadioButton)findViewById(R.id.edit_male);
        edit_female=(RadioButton)findViewById(R.id.edit_female);
        edit_secret=(RadioButton)findViewById(R.id.edit_secret);

        /*
        Intent intent=getIntent();
        int sid=intent.getIntExtra('sid',0);
        init_infor(sid);
        */
        init_infor(0);
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_infor(0);
            }
        });
    }

    /**
     * 初始化个人信息界面
     * 从数据库内读取个人信息并对页面内的对应控件进行赋值，包括头像，昵称，院系，性别，简介
     */
    private void init_infor(int s){
        //初始化
        String old_name="我的昵称";
        String old_department="我的院系";
        String old_introduction="我的个人简介";
        int sid=s;
        //final int old_sex=R.id.edit_male;---->查

        edit_name.setText(old_name.toCharArray(),0,old_name.length());
        edit_department.setText(old_department.toCharArray(),0,old_department.length());
        edit_introduction.setText(old_introduction.toCharArray(),0,old_introduction.length());
        edit_male.setChecked(true);

    }

    /**
     * 保存修改，返回给数据库
     */
    private void set_infor(int s){
        String new_name=edit_name.getText().toString();
        String new_department=edit_department.getText().toString();
        String new_introduction=edit_introduction.getText().toString();
        final int new_sex=edit_sex.getCheckedRadioButtonId();
        int sid=s;

        edit_name.setText(new_name.toCharArray(),0,new_name.length());
        edit_department.setText(new_department.toCharArray(),0,new_department.length());
        edit_introduction.setText(new_introduction.toCharArray(),0,new_introduction.length());
        if(edit_male.getId()==new_sex) edit_male.setChecked(true);
        else if(edit_female.getId()==new_sex) edit_female.setChecked(true);
        else if(edit_secret.getId()==new_sex) edit_secret.setChecked(true);

        /*
        传回数据库
         */

        //startActivity(new Intent(Edit_information.this,MainActivity.class));//test
    }
}
