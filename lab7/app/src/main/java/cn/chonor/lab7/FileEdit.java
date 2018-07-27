package cn.chonor.lab7;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Chonor on 2017/12/10.
 */

public class FileEdit extends AppCompatActivity {
    private EditText file_name=null;
    private EditText file_content=null;
    private Button save=null,load=null,clear=null,delete=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_edit);
        Init();
        Click_init();
    }

    /**
     *控件初始化
     */
    private void Init(){
        file_name=(EditText)findViewById(R.id.file_name);
        file_content=(EditText)findViewById(R.id.file_content);
        save=(Button)findViewById(R.id.save);
        load=(Button)findViewById(R.id.load);
        clear=(Button)findViewById(R.id.clear);
        delete=(Button)findViewById(R.id.delete);
    }

    /**
     * 控件点击事件
     */
    private void Click_init(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //保存
                String name=file_name.getText().toString().replace("/","_");
                String content=file_content.getText().toString();
                if(name.length()==0){//文件名为空 提示
                    Toast.makeText(FileEdit.this,"File Name cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else{//不为空保存
                    try(FileOutputStream fileOutputStream = openFileOutput(name,MODE_PRIVATE)){
                        fileOutputStream.write(content.getBytes());//写入
                        Toast.makeText(FileEdit.this,"Save succesfully",Toast.LENGTH_SHORT).show();
                    }catch (IOException e){//保存失败
                        Toast.makeText(FileEdit.this,"Fail to save file",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","Fail to save file");
                    }
                }
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//载入
                String name=file_name.getText().toString().replace("/","_");
                if(name.length()==0){//文件名为空 提示
                    Toast.makeText(FileEdit.this,"File Name cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else{//读入文件载入
                    try(FileInputStream fileInputStream =openFileInput(name)){
                        byte[] contents= new byte[fileInputStream.available()];
                        fileInputStream.read(contents);//填充控件
                        file_content.setText(new String(contents));
                        Toast.makeText(FileEdit.this,"Load succesfully",Toast.LENGTH_SHORT).show();
                    }catch (IOException e){//载入失败
                        Toast.makeText(FileEdit.this,"Fail to load file",Toast.LENGTH_SHORT).show();
                        Log.e("TAG","Fail to save file");
                    }
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//清理
                file_content.setText("");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//删除
                String name=file_name.getText().toString().replace("/","_");
                if(name.length()==0){//文件名为空提示
                    Toast.makeText(FileEdit.this,"File Name cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(deleteFile(name)) {//删除成功
                        Toast.makeText(FileEdit.this,"Delete succesfully",Toast.LENGTH_SHORT).show();
                    }else {//删除失败
                        Toast.makeText(FileEdit.this,"Fail to Delete file",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
