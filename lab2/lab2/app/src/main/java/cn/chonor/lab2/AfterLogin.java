package cn.chonor.lab2;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.view.ViewGroup.LayoutParams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.w3c.dom.Text;

import java.io.InputStream;
/**
 * Created by Chonor on 2017/10/16.
 */

public class AfterLogin  extends AppCompatActivity {
    private Button login1=null;
    private Button login2=null;
    private Button login3=null;
    private Button login4=null;
    private Button login5=null;
    private Button login6=null;
    private Button exit=null;
    private ImageView iImage=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_login);
        login1=(Button)findViewById(R.id.button3);
        login2=(Button)findViewById(R.id.button4);
        login3=(Button)findViewById(R.id.button5);
        login4=(Button)findViewById(R.id.button6);
        login5=(Button)findViewById(R.id.button7);
        login6=(Button)findViewById(R.id.button8);
        exit=(Button)findViewById(R.id.button9);
        iImage=(ImageView)findViewById(R.id.imageView2);
        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(AfterLogin.this,MainActivity.class);
                startActivity(i);
                Snackbar.make(exit, "登出成功", Snackbar.LENGTH_SHORT)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Snackbar的确定按钮被点击了", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });
        login1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(login1.getVisibility()==View.VISIBLE){
                    login1.setVisibility(View.INVISIBLE);
                    login4.setVisibility(View.VISIBLE);
                }
            }
        });
        login4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(login4.getVisibility()==View.VISIBLE){
                    login4.setVisibility(View.INVISIBLE);
                    login1.setVisibility(View.VISIBLE);
                }
            }
        });
        login2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(login2.getVisibility()==View.VISIBLE){
                    login2.setVisibility(View.INVISIBLE);
                    login5.setVisibility(View.VISIBLE);
                }
            }
        });
        login5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(login5.getVisibility()==View.VISIBLE){
                    login5.setVisibility(View.INVISIBLE);
                    login2.setVisibility(View.VISIBLE);
                }
            }
        });
        login6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                iImage.setImageDrawable(getResources().getDrawable(R.mipmap.tuixue));
                Snackbar.make(login6, "您的的退学申请已经提交", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "感谢您的确认", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .show();
            }
        });
    }

}
