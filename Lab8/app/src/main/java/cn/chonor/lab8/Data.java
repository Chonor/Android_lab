package cn.chonor.lab8;

/**
 * Created by Chonor on 2017/12/17.
 */

public class Data {
    private int id;
    private String name;
    private String birth;
    private String gift;

    public Data(){

    }
    public Data(String name,String birth,String gift){
        this.name=name;
        this.birth=birth;
        this.gift=gift;
    }
    public Data( int id,String name,String birth,String gift){
        this.id=id;
        this.name=name;
        this.birth=birth;
        this.gift=gift;
    }
    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setBirth(String birth){this.birth=birth;}
    public void setGift(String gift){this.gift=gift;}
    public int getId(){return id;}
    public String getName(){return name;}
    public String getBirth(){return birth;}
    public String getGift(){return gift;}
}
