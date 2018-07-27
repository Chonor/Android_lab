package cn.chonor.lab8;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button Add;
    private ListView listView;
    private MyAdapter myAdapter;
    private DatabaseHelper databaseHelper = new DatabaseHelper(this);
    private ArrayList<Data> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(MainActivity.this);
        //databaseHelper.Drop_Tab("Birth_DATA");
        Init();
        Init_listener();
    }

    /**
     * 控件获取
     * 数据库内容拉取填充
     */
    private void Init() {
        Add = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listview);
        try {
            if(!databaseHelper.tabIsExist("Birth_DATA")) {//主界面数据库
                databaseHelper.Create(); //不存在数据表则创建
            }
            datas = databaseHelper.Select_ALL(); //获取全部数据
        }catch (SQLException e){

        }
        myAdapter = new MyAdapter(this, datas);
        listView.setAdapter(myAdapter);
    }

    /**
     * 点击事件
     */
    private void Init_listener() {
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Additem.class);
                startActivity(i);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {//单击修改
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);//自定义dialog
                final View view1 = inflater.inflate(R.layout.dialoglayout, null);
                final TextView name = (TextView) view1.findViewById(R.id.dialog_name);
                final EditText birth = (EditText) view1.findViewById(R.id.dialog_birth);
                final EditText gift = (EditText) view1.findViewById(R.id.dialog_gift);
                final TextView phone = (TextView) view1.findViewById(R.id.dialog_phone);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //填充一波数据
                name.setText("姓名："+datas.get(i).getName());
                phone.setText("电话："+getPhone(datas.get(i).getName()));
                birth.setText(datas.get(i).getBirth());
                gift.setText(datas.get(i).getGift());
                builder.setView(view1);
                builder.setTitle("真是有毒性的颜文字呢...");
                builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {//确定保存
                        Data data=new Data(datas.get(i).getId(),datas.get(i).getName(),birth.getText().toString(),gift.getText().toString());
                        databaseHelper.Update(data); //数据库修改
                        datas.remove(i);
                        myAdapter.notifyDataSetChanged();//更新列表
                        datas.add(i,data);
                        myAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("放弃修改", new DialogInterface.OnClickListener() { //放弃无视
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {

                    }
                });
                builder.setCancelable(true);//误点击可取消
                builder.create().show();

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {//长按删除
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否删除?");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        databaseHelper.Delete(datas.get(i)); //修改数据库
                        datas.remove(i);
                        myAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setCancelable(true);//误点击可取消
                builder.create().show();
                return true;
            }
        });
    }


    /**
     * 获取联系人列表
     * @param name 指定联系人名字
     * @return 联系人电话
     */
    private String getPhone(String name) {
        //读取联系人列表
        String Phone = "无";
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {//获取全部联系人
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String name_c = cursor.getString(cursor.getColumnIndex("display_name"));
            if (name.equals(name_c)) { //判断名字是否是需要的
                Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);
                while (phone.moveToNext()) {//查询获取电话
                    Phone = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
                }
                if (phone != null) phone.close();
            }
        }
        if (cursor != null) cursor.close();
        return Phone;
    }

    private static final int REQUEST_READ_CONTACTS=0;
    private static String[] PERMISSIONS_READ_CONTACTS = {
            Manifest.permission.READ_CONTACTS};
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS);//直接检测权限
        if (permission != PackageManager.PERMISSION_GRANTED) { //没有就去申请
            ActivityCompat.requestPermissions(activity, PERMISSIONS_READ_CONTACTS,REQUEST_READ_CONTACTS );
        }
    }

}
