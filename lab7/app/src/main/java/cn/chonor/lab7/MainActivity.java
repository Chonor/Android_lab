package cn.chonor.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText pass1=null;
    private EditText pass2=null;
    private EditText passok=null;
    private Button ok=null;
    private Button clear=null;
    private boolean flag=false;
    protected String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Click_init();
    }

    /**
     * 检测 是否已经存在密码
     */
    private void Exist_Passw(){
        SharedPreferences preferences=getSharedPreferences("pw",MODE_PRIVATE);
        pass=preferences.getString("pw",null);
        flag=(pass!=null);
        if(flag){
            pass=DES.decryptDES(pass);
        }
    }
    /**
     *控件初始化
     */
    private void Init(){
        pass1=(EditText)findViewById(R.id.pass1);
        pass2=(EditText)findViewById(R.id.pass2);
        passok=(EditText)findViewById(R.id.passok);
        ok=(Button)findViewById(R.id.OK);
        clear=(Button)findViewById(R.id.CLEAR);
        Exist_Passw();
        //修改布局
        if(flag){//密码存在
            pass1.setVisibility(View.GONE);
            pass2.setVisibility(View.GONE);
            passok.setVisibility(View.VISIBLE);
        }
        else{//密码不存在
            pass1.setVisibility(View.VISIBLE);
            pass2.setVisibility(View.VISIBLE);
            passok.setVisibility(View.GONE);
        }
    }

    /**
     * 设置点击事件监听器
     */
    private void Click_init(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag){//密码存在
                    String pw=passok.getText().toString();
                    if(pw.length()==0)//密码为空
                        Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    else if(pass.equals(MD5Utils.md5Password(pw))){//不为空比较MD5判断
                        //密码匹配转跳
                        Intent i=new Intent(MainActivity.this,FileEdit.class);
                        startActivity(i);
                        finish();//直接销毁主界面
                    }
                    else //密码不匹配
                        Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                }else{//密码不存在
                    String pw1=pass1.getText().toString();
                    String pw2=pass2.getText().toString();
                    if(pw1.length()==0||pw2.length()==0)//密码为空
                        Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_SHORT).show();
                    else if(pw1.equals(pw2)){//两次输入密码相等
                        //存入密码
                        SharedPreferences preferences=getSharedPreferences("pw", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("pw", DES.encryptDES(pw1));//MD5加密
                        editor.commit();
                        //界面转跳
                        Intent i=new Intent(MainActivity.this,FileEdit.class);
                        startActivity(i);
                        finish();//销毁当前界面
                    }
                    else{//密码不匹配
                        Toast.makeText(MainActivity.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
                        pass2.setText("");//清空一个
                    }
                }
            }
        });
        //清空输入框
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass1.setText("");
                pass2.setText("");
                passok.setText("");
            }
        });
    }

}
