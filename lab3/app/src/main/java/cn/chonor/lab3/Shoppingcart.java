package cn.chonor.lab3;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Chonor on 2017/10/20.
 */

public class Shoppingcart extends AppCompatActivity {
    private ListView listView;
    private Data data = new Data();
    private FloatingActionButton floatingActionButton = null;
    private AlertDialog.Builder builder = null;
    private MyAdapter myAdapter;
    private TextView all;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping);
        all=(TextView)findViewById(R.id.all);
        get_set_data();//接收数据
        init();//初始化
        init_listener();//事件监听
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) { //接收返回数据
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            ArrayList<Good> tmp= extras.getParcelableArrayList("data");
            ArrayList<Good> tmp1= extras.getParcelableArrayList("cart");
            data.setGood_list(tmp);
            data.setCart_list(tmp1);
            myAdapter = new MyAdapter(this, data.getCart_list(), 0);
            listView.setAdapter(myAdapter);
        }
    }
    private void get_set_data(){ //接收数据
        Bundle extras = getIntent().getBundleExtra("mainActivity");
        if (extras != null) {
            ArrayList<Good> tmp = extras.getParcelableArrayList("data");
            ArrayList<Good> tmp1 = extras.getParcelableArrayList("cart");
            data.setGood_list(tmp);
            data.setCart_list(tmp1);
        }
        //购物车时增加一列空
        if (data.getCart_list().size() == 0 || !data.getCart_list().get(0).getGoodName().equals("购物车")) {
                Good good = new Good("购物车", "价格", " ", " ");
            good.setGoodFisrt("*");
            data.addCart_list(0, good);
        }
        double sum=0;
        for(int i=1;i<data.getCart_list().size();i++){
            sum+= data.getCart_list_index(i).getCnt()* data.getCart_list_index(i).getPrices();
        }

        all.setText("总价：￥ "+ String.valueOf(sum));
    }
    private void init(){//初始化

        builder = new AlertDialog.Builder(this);
        listView = (ListView) this.findViewById(R.id.myListView);
        myAdapter = new MyAdapter(this, data.getCart_list(), 0);
        listView.setAdapter(myAdapter);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton1);
    }
    private void  init_listener(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() { //按钮转跳
            @Override
            public void onClick(View view) { //设置数据
                Intent i = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("data", data.getGood_list());
                bundle.putParcelableArrayList("cart", data.getCart_list());
                i.putExtras(bundle);
                setResult(RESULT_OK, i);
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//单击进入详细
                if (data.getCart_list().get(i).getGoodName().equals("购物车")) {
                } else {//数据传输
                    Intent intent = new Intent(Shoppingcart.this, Good_Info.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putParcelableArrayList("data", data.getGood_list());
                    bundle.putParcelableArrayList("cart", data.getCart_list());
                    intent.putExtra("shoppingcart", bundle);
                    startActivityForResult(intent, 2);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按提示删除
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (data.getCart_list().get(i).getGoodName().equals("购物车")) {
                } else {
                    alertdialog_build(i);
                    AlertDialog dialog = builder.create(); //完成创建AlertDialog并显示
                    dialog.show();
                    double sum=0;
                    for(int j=1;j<data.getCart_list().size();j++){
                        sum+= data.getCart_list_index(j).getCnt()* data.getCart_list_index(j).getPrices();
                    }

                    all.setText("总价：￥ "+ String.valueOf(sum));
                }
                return true;
            }
        });
    }
    public void alertdialog_build(final int position) {
        //alertdialog 初始化
        builder.setTitle("移除商品 ");
        final String tmp = data.getCart_list().get(position).getGoodName();
        builder.setMessage("从购物车移除" + tmp);//设置显示文本
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override //设置确定按钮动作
            public void onClick(DialogInterface dialogInterface, int i) {
                data.removeCart_list_index(position);
                myAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "成功删除 " + tmp, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override //设置取消按钮动作
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "您选择了取消", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true); //允许取消
    }
}
