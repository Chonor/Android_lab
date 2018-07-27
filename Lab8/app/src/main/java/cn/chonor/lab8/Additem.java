package cn.chonor.lab8;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Chonor on 2017/12/17.
 */

public class Additem extends AppCompatActivity {
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private Button button;
    private EditText name;
    private EditText birth;
    private EditText gift;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        Init();
        Init_Listener();
    }

    private void Init(){
        button=(Button)findViewById(R.id.add);
        name=(EditText)findViewById(R.id.name1);
        birth=(EditText)findViewById(R.id.brith1);
        gift=(EditText)findViewById(R.id.gift1);
    }
    private void Init_Listener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data=new Data(name.getText().toString(),birth.getText().toString(),gift.getText().toString());
                if(data.getName().length()==0){//姓名为空
                    Toast.makeText(Additem.this,"姓名为空，请完善",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(databaseHelper.Insert_ReID(data)==-1) {//查询数据库
                        Toast.makeText(Additem.this, "姓名重复，请检查", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent=new Intent(Additem.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
