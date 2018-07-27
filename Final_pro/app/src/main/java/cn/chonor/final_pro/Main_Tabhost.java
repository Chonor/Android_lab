package cn.chonor.final_pro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.chonor.final_pro.adapter.ChatAdapter;
import cn.chonor.final_pro.adapter.ClassInfoAdapter;
import cn.chonor.final_pro.adapter.ClassListAdapter;


public class Main_Tabhost extends AppCompatActivity implements View.OnClickListener{

    public void Click_Today_Class(View v,int position){ //今日课程点击事件
        Toast.makeText(this,"点击",Toast.LENGTH_SHORT).show();
    }
    public void Submit_SearchView(String query){ //课程列表的搜索栏提交事件
        Toast.makeText(this,query,Toast.LENGTH_SHORT).show();
    }
    public void Click_Class_List(View v,int position){ //课程列表点击事件
        Toast.makeText(this,"点击",Toast.LENGTH_SHORT).show();
    }
    public void Long_Click_Class_List(View v,int position){ //课程列表长按事件
        Toast.makeText(this,"长按",Toast.LENGTH_SHORT).show();
    }
    public void Click_Chat_item(View v,int position,int which){ //聊天吐槽界面中的item点击事件，position无须赘述，which是判断种类
        if(which==0){
            Toast.makeText(Main_Tabhost.this,"你点击了赞 position为"+position,Toast.LENGTH_SHORT).show();
        }
        else if(which==1){
            Toast.makeText(Main_Tabhost.this,"你点击了踩 position为"+position,Toast.LENGTH_SHORT).show();
        }
        else if(which==2){
            Toast.makeText(Main_Tabhost.this,"你点击了举报 position为"+position,Toast.LENGTH_SHORT).show();
        }
    }

    public List<Map<String,String>> my_class=new ArrayList<>();//今日课程
    ClassInfoAdapter classInfoAdapter;//今日课程
    public SearchView searchView;
    public List<Map<String,String>> class_list=new ArrayList<>();//课程列表
    ClassListAdapter classListAdapter;//课程列表
    public List<Map<String,String>> chat_list=new ArrayList<>();//闲聊区
    ChatAdapter chatAdapter;//闲聊区

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tabhost);

        findViewById(R.id.add_new_chat).setOnClickListener(this);
        Init_TabHost();
        Init_Today_Class_RecyclerView();
        Init_Class_List_RecyclerView();
        Init_SearchView();
        Init_ChatView();
        Init_My_Info();
        //以下为接口函数的演示，可删去
        Add_Today_Class("人工智能","16:15~18:00 东B202","任课教师:饶阳辉");
        Update_Today_Class(0,"人工智障","16:15~18:00 东B202","任课教师:饶阳辉");
        Add_Class_list("111","111","111","111","111");
        Change_Mark(0,"visible");
        Add_Chat_List("http://imgstore04.cdn.sogou.com/app/a/100520024/877e990117d6a7ebc68f46c5e76fc47a","test1","学院1","正文1","gone",4,3);
        Add_Chat_List("http://oxndvstlm.bkt.clouddn.com/lab2-double-1.png","test2","学院2","正文2","http://oxndvstlm.bkt.clouddn.com/lab2-2.png",9,6);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.avatar){ //个人中心界面点击头像

        }
        else if(v.getId()==R.id.my_class_list){ //个人中心界面点击"我的课程列表"

        }
        else if(v.getId()==R.id.edit_my_info) { //个人中心界面点击"编辑我的信息"

        }
        else if(v.getId()==R.id.view_my_notice){ //个人中心界面点击"查看我的通知"

        }
        else if(v.getId()==R.id.system_setting){ //个人中心界面点击"系统设置"

        }
        else if(v.getId()==R.id.sign_out){ //个人中心界面点击"退出登录"

        }
        else if(v.getId()==R.id.add_new_chat){
            startActivity(new Intent("ADD_NEW_CHAT"));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){ //这里是给课程表设置宽高和背景
        GridLayout gridLayout=(GridLayout)findViewById(R.id.grid);
        int screen_width=this.getWindowManager().getDefaultDisplay().getWidth();
        int height=gridLayout.getHeight();
        for(int i=0;i<gridLayout.getChildCount();i++){
            TextView textView=(TextView) gridLayout.getChildAt(i);
            textView.setHeight(height/5);
            Log.i("TextView_height",""+textView.getHeight());
            if(i%8==0){
                textView.setBackgroundColor(getResources().getColor(R.color.col_bg));
                int tmp=i%8+i/8+1;
                textView.setText("   "+tmp);
                textView.setWidth((screen_width/8));
                textView.setTextSize(20);
            }
            else{
                textView.setTextSize(15);
                if(i%8==1) textView.setWidth((screen_width/8));
                else textView.setWidth((screen_width/8)-6);
                Random random=new Random();
                int rand=random.nextInt(4);
                if(rand==0){
                    textView.setBackgroundResource(R.drawable.class_bg);
                    textView.setText("云计算@东B402");
                }
                else if(rand==1){
                    textView.setBackgroundResource(R.drawable.class_bg2);
                    textView.setText("人工智能@东B202");
                }
                else if(rand==2) {
                    textView.setBackgroundResource(R.drawable.class_bg3);
                    textView.setText("数据库@东B201");
                }
                else{
                    textView.setText("");
                    textView.setBackgroundResource(R.drawable.class_empty_bg);
                }
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }


    public void Init_TabHost(){
        TabHost tabHost=(TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("one").setIndicator("我的课程").setContent(R.id.我的课程));
        tabHost.addTab(tabHost.newTabSpec("two").setIndicator("今日课程").setContent(R.id.今日课程));
        tabHost.addTab(tabHost.newTabSpec("three").setIndicator("课程列表").setContent(R.id.课程列表));
        tabHost.addTab(tabHost.newTabSpec("four").setIndicator("下课聊").setContent(R.id.下课聊));
        tabHost.addTab(tabHost.newTabSpec("five").setIndicator("我").setContent(R.id.我));
    }
    public void Init_Today_Class_RecyclerView(){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.today_class);//今日课程
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        classInfoAdapter=new ClassInfoAdapter(this,my_class);
        classInfoAdapter.setItemClickListener(new ClassInfoAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View v, int position) {
                Click_Today_Class(v,position);
            }

            @Override
            public void OnLongClick(View v, int position) {
                //Toast.makeText(MainActivity.this,"长按",Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(classInfoAdapter);
    }
    public void Init_Class_List_RecyclerView(){
        RecyclerView class_list_view=(RecyclerView)findViewById(R.id.class_list_recycler_view);
        class_list_view.setLayoutManager(new LinearLayoutManager(this));
        classListAdapter=new ClassListAdapter(this,class_list);
        classListAdapter.setItemClickListener(new ClassListAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View v, int position) {
                Click_Class_List(v,position);
            }

            @Override
            public void OnLongClick(View v, int position) {
                Long_Click_Class_List(v,position);
            }
        });
        class_list_view.setAdapter(classListAdapter);
    }
    public void Init_SearchView(){
        searchView=(SearchView)findViewById(R.id.class_list_searchview);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("请输入课程名称");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Submit_SearchView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void Init_ChatView(){
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.chat_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter=new ChatAdapter(this,chat_list);
        chatAdapter.setItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View v, int position, int which) {
                Click_Chat_item(v,position,which);
            }
        });
        recyclerView.setAdapter(chatAdapter);
    }
    public void Init_My_Info(){
        int screen_width=this.getWindowManager().getDefaultDisplay().getWidth();
        screen_width*=0.9;
        Button my_class_list=(Button) findViewById(R.id.my_class_list);
        my_class_list.setWidth(screen_width);
        Button edit_my_info=(Button) findViewById(R.id.edit_my_info);
        edit_my_info.setWidth(screen_width);
        Button view_my_notice=(Button) findViewById(R.id.view_my_notice);
        view_my_notice.setWidth(screen_width);
        Button system_setting=(Button) findViewById(R.id.system_setting);
        system_setting.setWidth(screen_width);
        Button sign_out=(Button) findViewById(R.id.sign_out);
        sign_out.setWidth(screen_width);

        findViewById(R.id.avatar).setOnClickListener(this);
        my_class_list.setOnClickListener(this);
        edit_my_info.setOnClickListener(this);
        view_my_notice.setOnClickListener(this);
        system_setting.setOnClickListener(this);
        sign_out.setOnClickListener(this);
    }

    public void Add_Today_Class(String name,String time_and_place,String teacher){ //添加今日课程
        Map<String,String> tmp=new HashMap<>();
        tmp.put("class_name",name);
        tmp.put("time_and_place",time_and_place);
        tmp.put("teacher",teacher);
        tmp.put("img","gone");
        my_class.add(tmp);
        classInfoAdapter.notifyDataSetChanged();
    }
	public void Update_Today_Class(int position,String name,String time_and_place,String teacher){
		my_class.get(position).put("class_name",name);
        my_class.get(position).put("time_and_place",time_and_place);
        my_class.get(position).put("teacher",teacher);
        classInfoAdapter.notifyDataSetChanged();
	}
	public void Delete_Today_Class(int position){
        my_class.remove(position);
        classInfoAdapter.notifyDataSetChanged();
    }
    public void Change_Notice(int position,String type){ //传入item的位置和type，比如要这个position的小红点显示，type=visible; 要隐藏这个position的小红点就type=其他任何值
        if(type.equals("visible")){
            my_class.get(position).put("img","visible");
        }
        else my_class.get(position).put("img","gone");
        classInfoAdapter.notifyDataSetChanged();
    }
    public void Change_No_Login_Hint(String type){ //未登录的提示，type=visible可见，其他值隐藏
        if(type.equals("visible")){
            findViewById(R.id.no_login_hint).setVisibility(View.VISIBLE);
        }
        else findViewById(R.id.no_login_hint).setVisibility(View.GONE);
    }
    public void Add_Class_list(String name,String time_and_place,String teacher,String starting_unit,String class_info){
        Map<String,String> tmp=new HashMap<>();
        tmp.put("class_name",name);
        tmp.put("time_and_place",time_and_place);
        tmp.put("teacher",teacher);
        tmp.put("starting_unit",starting_unit);
        tmp.put("class_info",class_info);
        tmp.put("mark","gone");
        class_list.add(tmp);
        classListAdapter.notifyDataSetChanged();
    }
    public void Clear_Class_list(){
        class_list.clear();
        classListAdapter.notifyDataSetChanged();
    }
    public void Change_Mark(int position,String type){ //传入item的位置和type，比如要这个position的对勾显示，就让type=visible，隐藏就是其他任何值
        if(type.equals("visible")){
            class_list.get(position).put("mark","visible");
        }
        else class_list.get(position).put("mark","gone");
        classListAdapter.notifyDataSetChanged();
    }
    public void Add_Chat_List(String chat_avatar,String nickname,String college,String main_text,String user_image,int up_num,int down_num){
        Map<String,String> tmp=new HashMap<>();
        tmp.put("chat_avatar",chat_avatar);
        tmp.put("nickname",nickname);
        tmp.put("college",college);
        tmp.put("main_text",main_text);
        tmp.put("user_image",user_image);
        tmp.put("up_num",up_num+"");
        tmp.put("down_num",down_num+"");
        chat_list.add(tmp);
        chatAdapter.notifyDataSetChanged();
    }
    public void Delete_Chat_List(int position){
        class_list.remove(position);
        chatAdapter.notifyDataSetChanged();
    }
}

