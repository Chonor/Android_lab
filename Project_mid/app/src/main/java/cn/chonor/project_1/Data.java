package cn.chonor.project_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by Chonor on 2017/11/14.
 */

public class Data  implements Parcelable {
    private String name,place,info;
    private int sex,both,dead,id;
    private Bitmap bitmap;
    public Data(){

    }
    public Data(String name,String place,String info,int sex,int both,int dead,Bitmap bitmap){
        this.name=name;
        this.place=place;
        this.info=info;
        this.sex=sex;
        this.both=both;
        this.dead=dead;
        this.bitmap=bitmap;
    }
    public Data(Data data){
        //this.id=data.id;
        this.name= data.name;
        this.place= data.place;
        this.info= data.info;
        this.sex= data.sex;
        this.both= data.both;
        this.dead= data.dead;
        this.bitmap=data.bitmap;
    }
    public void setId(int id){
        this.id=id;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setPlace(String place){
        this.place=place;
    }
    public void setInfo(String info){
        this.info=info;
    }
    public void setSex(int sex){
        this.sex=sex;
    }
    public void setBoth_and_Dead(int both,int dead){
        this.both=both;
        this.dead=dead;
    }
    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    public String getName(){
        return name;
    }
    public String getPlace(){
        return place;
    }
    public String getInfo(){
        return info;
    }
    public String getBoth_and_Dead(){
        return String.valueOf(both)+" - "+String.valueOf(dead);
    }
    public int getSex(){
        return sex;
    }
    public int getId(){
        return id;
    }
    public int getBoth(){
        return both;
    }
    public int getDead(){
        return dead;
    }
    public Bitmap getBitmap(){
        return bitmap;
    }
    public byte[] getbyte(){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    public void setbyte(byte[] in){
       setBitmap(BitmapFactory.decodeByteArray(in, 0, in.length));
    }
    @Override
    public int describeContents() {
        return 7;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(place);
        parcel.writeString(info);
        parcel.writeInt(sex);
        parcel.writeInt(both);
        parcel.writeInt(dead);
        parcel.writeInt(id);
       // parcel.writeByteArray(getbyte());
    }
    public static final Parcelable.Creator<Data> CREATOR =new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source){
            Data data= new Data();
            data.name=source.readString();
            data.place=source.readString();
            data.info=source.readString();
            data.sex=source.readInt();
            data.both=source.readInt();
            data.dead=source.readInt();
            data.id=source.readInt();
            /*byte in[]=new byte[]{};
            source.readByteArray(in);
            data.setbyte(in);*/
            return data;
        }
        @Override
        public Data[] newArray(int size){
            return new Data[size];
        }
    };
}
