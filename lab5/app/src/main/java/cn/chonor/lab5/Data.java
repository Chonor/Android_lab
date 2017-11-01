package cn.chonor.lab5;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chonor on 2017/10/20.
 */

public class Data  {

    private String Name[]={"Enchated Forest","Arla Milk","Devondale Milk","Kindle Oasis","waitrose 早餐麦片","Mcvitie's 饼干","Ferrero Rocher","Maltesers","Lindt","Borggreve"};
    private String Price[]={"¥ 5.00","¥ 59.00","¥ 79.00","¥ 2399.00","¥ 179.00","¥ 14.90","¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
    private String Info[]={"Johanna Basford","德国","澳大利亚","8GB","2Kg","英国","300g","118g","249g","640g"};
    private String Types[]={"作者","产地","产地","版本","重量","产地","重量","重量","重量","重量"};
    public int ID[]={R.mipmap.enchatedforest,R.mipmap.arla,R.mipmap.devondale,R.mipmap.kindle,R.mipmap.waitrose,R.mipmap.mcvitie,R.mipmap.ferrero,R.mipmap.maltesers,R.mipmap.lindt,R.mipmap.borggreve};
    private double Prices[]={5.00,59.00,79.00,2399.00,179.00,14.90,132.59,141.43,139.43,28.90};
    private ArrayList<Good> good_list=null;
    private ArrayList<Good> cart_list=null;
    public Map<String,Integer>map= new HashMap<String,Integer>();
    public Data() {
        init();
    }

    public void init() {
        good_list=new ArrayList<Good>();
        cart_list=new ArrayList<Good>();
        for (int i = 0; i < Name.length; i++) {
            Good good = new Good();
            good.setGoodName(Name[i]);
            good.setGoodPrice(Price[i]);
            good.setGoodInfo(Info[i]);
            good.setGoodTypes(Types[i]);
            good.setGoodPrices(Prices[i]);
            good_list.add(good);
            map.put(Name[i],i);


        }

    }
    //返回商品列表的一个
    public Good getGood_list_index(int i){return good_list.get(i);}
    //删除商品列表中的一个
    public void removeGood_list_index(int i){
        good_list.remove(i);
    }
    //返回商品列表
    public ArrayList<Good> getGood_list() {
        return good_list;
    }
    //重设商品列表
    public void  setGood_list(ArrayList<Good> arrayList){good_list=arrayList;}
    //设置商品列表中的收藏表示
    public void setGood_list_Stat(int i){
        Good tmp=good_list.get(i);
        tmp.setStar();
        good_list.set(i,tmp);
    }
    //返回购物车列表的一个
    public Good getCart_list_index(int i){return cart_list.get(i);}
    //在购物车列表中指定位置增加一个
    public void addCart_list(int i,Good good){
        cart_list.add(i,good);
    }
    //在购物车列表中增加一个
    public void addCart_list(Good good){
        good.setCnt(1);
        cart_list.add(good);
    }
    // 重设购物车列表
    public void setCart_list(ArrayList<Good> arrayList){
        cart_list=arrayList;
    }
    //返回购物车列表
    public ArrayList<Good> getCart_list(){return cart_list;}
    //移除购物车列表中的一个
    public void removeCart_list_index(int i){
        cart_list.remove(i);
    }
    //设置购物车列表中的商品的数量
    public void setCart_list_Cnt(int i,int cnt){
        Good tmp=cart_list.get(i);
        tmp.setCnt(cnt);
        cart_list.set(i,tmp);
    }
    //设置购物车列表中的商品收藏
    public void setCart_list_Stat(int i){
        Good tmp=cart_list.get(i);
        tmp.setStar();
        cart_list.set(i,tmp);
    }
}
