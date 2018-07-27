package cn.chonor.final_pro.login_register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.chonor.final_pro.notice.Notice_receive;
import cn.chonor.final_pro.R;

//Mainactivity负责登录
public class LoginActivity extends AppCompatActivity {
    private Button button_login;
    private TextView link_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("登录账户");

        button_login = (Button) findViewById(R.id.login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText login_sid=(EditText)findViewById(R.id.login_sid);
                EditText login_password=(EditText)findViewById(R.id.login_password);
                /*
                查询数据库，判断账号是否存在，sid和密码是否匹配
                 */
                /*
                if(){
                    toast = Toast.makeText(getApplicationContext(),"账户不存在", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else if(){
                    toast = Toast.makeText(getApplicationContext(),"密码错误", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                else{
                    jump_to_userCenter();
                }
                 */
                startActivity(new Intent(LoginActivity.this,Notice_receive.class));
                Toast.makeText(getApplicationContext(),"登录成功，跳转到个人中心",Toast.LENGTH_SHORT).show();
            }
        });

        link_register = (TextView) findViewById(R.id.gister_link);
        link_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jump_to_register();
            }
        });
    }

    //跳转用户个人中心界面
    public void jump_to_userCenter() {
        Toast.makeText(getApplicationContext(),"跳转到个人中心(待补充)",Toast.LENGTH_SHORT).show();
    }
    //跳转注册界面
    public void jump_to_register() {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}


