package cn.chonor.final_pro.notice;

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

/**
 * 这里是通知列表
 */
public class Notice_receive extends AppCompatActivity {
    private ListView notice_list ;
    ArrayList<HashMap<String,String>> arrayList=new ArrayList<HashMap<String,String>>();
    int sid;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_receive_activity);
        setTitle("我的通知");

        sid=0;
        //Intent intent=getIntent();
       // sid=intent.getIntExtra("sid",0);

        String map_key[]={"notice_title","notice_date","notice_content"};
        getData(sid);
        notice_list= (ListView) findViewById(R.id.notice_receive_list);
        SimpleAdapter adapter=new SimpleAdapter(this,arrayList,R.layout.listview_item,map_key,new int[]{R.id.item_notice_title,R.id.item_notice_date,R.id.item_notice_content});
        notice_list.setAdapter(adapter);

        notice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Notice_receive.this,Notice_information.class);

                ListView temp_listView = (ListView)adapterView;
                HashMap<String, String> map = (HashMap<String,String>) temp_listView.getItemAtPosition(i);
                String notice_title = map.get("notice_title");
                String notice_date = map.get("notice_date");
                String notice_content = map.get("notice_content");
                intent.putExtra("notice_title",notice_title);
                intent.putExtra("notice_date",notice_date);
                intent.putExtra("notice_content",notice_content);

                startActivity(intent);
            }
        });
    }

    /**
     * 从数据库里查出该用户的通知的数量以及相关信息，每次数据库改动，都要更新ListView--->noticechangexxxx
     * @return
     */
    private void getData(int s){


        //查询得到通知的数目以及标题和内容
        int notice_size=0;

        if(notice_size==0){
            HashMap<String, String> tempHashMap = new HashMap<String, String>();
            tempHashMap.put("notice_title", "还没有通知");
            tempHashMap.put("notice_date", "");
            tempHashMap.put("notice_content","");
            arrayList.add(tempHashMap);
        }
        else{
            for(int i=0;i<notice_size;i++){
                HashMap<String, String> tempHashMap = new HashMap<String, String>();
               // tempHashMap.put("notice_title", /**/);
                //tempHashMap.put("notice_date", /**/);
               // tempHashMap.put("notice_content", /**/);
                arrayList.add(tempHashMap);

            }
        }

    }


}