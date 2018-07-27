package cn.chonor.final_pro.Lesson;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import cn.chonor.final_pro.R;

/**
 * Created by Jy on 2017/12/24.
 */

public class Lesson_list extends AppCompatActivity {
    private ListView lesson_list;
    SimpleAdapter adapter;
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
    int sid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_list_activity);
        setTitle("课程列表");
        sid=0;
        //Intent intent=getIntent();
       // sid=intent.getIntExtra('sid',0);

        String map_key[]={"lesson_name","lesson_info","lesson_teacher"};
        getData();
        lesson_list=(ListView)findViewById(R.id.lesson_list);
        adapter=new SimpleAdapter(this,arrayList,R.layout.lesson_list_item,map_key,new int[]{R.id.item_lesson_name,R.id.item_lesson_info,R.id.item_lesson_teacher});
        lesson_list.setAdapter(adapter);

        lesson_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Lesson_list.this,Lesson_information.class);

                ListView temp_listView = (ListView)adapterView;
                HashMap<String, String> map = (HashMap<String,String>) temp_listView.getItemAtPosition(i);
                String lesson_name = map.get("lesson_name");
                String lesson_info = map.get("lesson_info");
                String lesson_teacher = map.get("lesson_teacher");
                intent.putExtra("lesson_name",lesson_name);
                intent.putExtra("lesson_info",lesson_info);
                intent.putExtra("lesson_teacher",lesson_teacher);

                startActivity(intent);
            }
        });

        lesson_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //删除，数据库也做出相对应改变
                new AlertDialog.Builder(Lesson_list.this)
                        .setTitle("确定删除该课程？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                arrayList.remove(which-1);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .create().show();

                return true;
            }
        });

    }


    /**
     * 从数据库里查出该用户的课程的数量以及相关信息，每次数据库改动，都要更新ListView
     * @return
     */
    private void getData(){


        //查询得到通知的数目以及标题和内容
        int lesson_size=0;

        if(lesson_size==0){
            HashMap<String, String> tempHashMap = new HashMap<String, String>();
            tempHashMap.put("lesson_name", "还没有课程");
            tempHashMap.put("lesson_info", "");
            tempHashMap.put("lesson_teacher","");
            arrayList.add(tempHashMap);
        }
        else{
            for(int i=0;i<lesson_size;i++){
                HashMap<String, String> tempHashMap = new HashMap<String, String>();
                // tempHashMap.put("lesson_name", /**/);
                //tempHashMap.put("lesson_info", /**/);
                // tempHashMap.put("lesson_teacher", /**/);
                arrayList.add(tempHashMap);

            }
        }
    }

}
