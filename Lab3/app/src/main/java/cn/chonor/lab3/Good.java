package cn.chonor.lab3;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Chonor on 2017/10/19.
 */

public class Good  implements Parcelable{
    private String name;
    private String price;
    private String info;
    private String types;
    private String first;
    private int cnt;
    private int star;
    public Good(){}
    public Good(String name,String  price,String info,String types){ //初始化
        setGoodName(name);
        setGoodInfo(info);
        setGoodTypes(types);
        setGoodPrice(price);
        cnt=0;
        star=0;
    }
    public void setGoodName(String name){//设置商品名称
        this.name=name;
        first= String.valueOf(this.name.charAt(0)).toUpperCase();
    }
    //设置商品信息
    public void setGoodFisrt(String first){
        this.first=first;
    }
    public void setGoodPrice(String price){
        this.price=price;
    }
    public void setGoodInfo(String info){
        this.info=info;
    }
    public void setGoodTypes(String types){
        this.types=types;
    }
    //设置商品收藏
    public void setStar(){
        if(star == 1)star=0;
        else star=1;
    }
    //设置商品数量
    public void setCnt(int cnt){this.cnt=cnt;}
    //返回商品数据
    public String getGoodName(){
        return name;
    }
    public String getGoodName_First(){
        return first;
    }
    public String getGoodPrice(){
        return price;
    }
    public String getGoodInfo(){
        return info;
    }
    public String getGoodTypes(){
        return types;
    }
    public int getCnt(){return cnt;}
    public int getStar(){return star;}

    //Parcelable类重载
    @Override
    public int describeContents() {
        return 7;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(info);
        parcel.writeString(types);
        parcel.writeString(first);
        parcel.writeInt(cnt);
        parcel.writeInt(star);
    }
    public static final Parcelable.Creator<Good> CREATOR =new Parcelable.Creator<Good>() {
        @Override
        public Good createFromParcel(Parcel source){
            Good good = new Good();
            good.name=source.readString();
            good.price=source.readString();
            good.info=source.readString();
            good.types=source.readString();
            good.first=source.readString();
            good.cnt=source.readInt();
            good.star=source.readInt();
            return good;
        }
        @Override
        public Good[] newArray(int size){
            return new Good[size];
        }
    };
}
