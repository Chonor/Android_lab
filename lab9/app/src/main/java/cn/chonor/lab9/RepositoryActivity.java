package cn.chonor.lab9;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import cn.chonor.lab9.adapter.ListViewAdapter;
import cn.chonor.lab9.model.Repositories;
import cn.chonor.lab9.service.GithubService;
import cn.chonor.lab9.service.ReposService;
import cn.chonor.lab9.service.ServiceFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Chonor on 2017/12/23.
 */

public class RepositoryActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar progressBar;
    private ArrayList<Repositories>arrayList;
    private ListViewAdapter adapter;
    private String name;
    public static final String GITHUB_API_URL = "https://api.github.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_repository);
        Init();
        Onloads();
    }

    private void Init(){
        listView=(ListView)findViewById(R.id.listView);
        progressBar=(ProgressBar)findViewById(R.id.user_bar);
        arrayList=new ArrayList<>();
    }
    private void Onloads(){
        Bundle extras = getIntent().getBundleExtra("main");
        if (extras != null) {
            name=extras.getString("name");
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            ReposService reposService= ServiceFactory.createRetrofit(GITHUB_API_URL).create(ReposService.class);
            reposService.getRepos(name)
                    .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ArrayList<Repositories>>() {
                        @Override
                        public void onCompleted() {//完成
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            //设置适配器
                            adapter=new ListViewAdapter(RepositoryActivity.this,arrayList);
                            listView.setAdapter(adapter);
                            Listener();
                        }
                        @Override
                        public void onError(Throwable e) {//出错
                            Toast.makeText(RepositoryActivity.this, e.hashCode() + "空", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(ArrayList<Repositories> repositories) {//获取全部的repos
                            arrayList=repositories;
                        }
                    });
        }

    }
    private void Listener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(RepositoryActivity.this,WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",arrayList.get(i).getHtml_url());
                intent.putExtra("repos",bundle);
                startActivity(intent);

            }
        });
    }
}
