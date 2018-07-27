package cn.chonor.lab9;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.chonor.lab9.adapter.RecyclerViewAdapter;
import cn.chonor.lab9.model.Github;
import cn.chonor.lab9.service.GithubService;
import cn.chonor.lab9.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    private Button clear;
    private Button fetch;
    private RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private EditText text;
    private ProgressBar progressBar;
    public static final String GITHUB_API_URL = "https://api.github.com";
    private ArrayList<Github>githubArrayList;
    private Map<String,Integer>map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Listener();
    }

    /**
     * 初始化
     */
    private void Init(){
        map=new HashMap<String, Integer>();
        githubArrayList = new ArrayList<>();
        clear=(Button)findViewById(R.id.clear);
        fetch=(Button)findViewById(R.id.fetch);
        recyclerView=(RecyclerView)findViewById(R.id.main_recycle);
        text=(EditText)findViewById(R.id.editText);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewAdapter(MainActivity.this,R.layout.main_item,githubArrayList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 监听点击事件
     */
    private void Listener(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//清空
                for(int i=githubArrayList.size()-1;i>=0;i--){
                    githubArrayList.remove(i);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info;
                info=text.getText().toString();
                if(info.length()==0){//判断为空
                    Toast.makeText(MainActivity.this,"输入为空",Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE); //切换显示
                    recyclerView.setVisibility(View.GONE);
                    GithubService githubService= ServiceFactory.createRetrofit(GITHUB_API_URL).create(GithubService.class);
                    githubService.getUser(info)
                            .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Github>() {
                                @Override
                                public void onCompleted() { //数据获取完成
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Throwable e) { //出错
                                    Toast.makeText(MainActivity.this,e.hashCode()+"请确认你搜索的用户存在",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onNext(Github github) {//获取到数据
                                    if(!map.containsKey(github.getLogin())){//如果没有加入到数据列表中
                                        githubArrayList.add(github);
                                        map.put(github.getLogin(),1);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                }
            }
        });
        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) { //跳转
                Intent intent=new Intent(MainActivity.this,RepositoryActivity.class);
                Bundle bundle = new Bundle(); //传递用户名
                bundle.putString("name",githubArrayList.get(position).getLogin());
                intent.putExtra("main",bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(final int position) {//长按弹出删除
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("是否删除");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//删除数据
                        map.remove(githubArrayList.get(position).getLogin());
                        githubArrayList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.show();
            }
        });
    }


}
