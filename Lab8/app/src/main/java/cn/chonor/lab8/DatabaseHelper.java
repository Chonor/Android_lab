package cn.chonor.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

/**
 * Created by Chonor on 2017/11/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String NAME = "LAB8.db";
    private static final String TABLE_NAME="Birth_DATA";
    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context) {
        super(context, NAME , null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                +" (id integer primary key autoincrement, "
                + "name varchar(50) UNIQUE NOT NULL, "
                + "birth varchar(50), "
                + "gift varchar(1000)) "
                );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void Create(){
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME
                +" (_id integer primary key autoincrement, "
                + "name varchar(50) UNIQUE NOT NULL, "
                + "birth varchar(50),  "
                + "gift varchar(1000)) "
        );
    }

    /**
     * 插入单个数据
     * @param data 数据类
     * @return 是否插入成功
     */
    private boolean Insert(Data data){
        ContentValues cv=new ContentValues();
        cv.put("name",data.getName());
        cv.put("birth",data.getBirth());
        cv.put("gift",data.getGift());
        if(Select_id(data.getName())==-1) {//判断是否存在
            getWritableDatabase().insert(TABLE_NAME, null, cv);
            return true;
        }
        else return false;
    }

    /**
     * 插入上数据并返回id
     * @param data 数据类
     * @return 插入成功返回id  失败-1
     */
    public int Insert_ReID(Data data){
        if(Insert(data)){
            return Select_id(data.getName());
        }
        return -1;
    }

    /**
     * 更新数据
     * @param data 源数据
     */
    public void Update(Data data){
        StringBuffer whereBuffer = new StringBuffer();
        whereBuffer.append("_id").append(" = ").append("'").append(data.getId()).append("'");
        ContentValues cv=new ContentValues();
        cv.put("birth",data.getBirth());
        cv.put("gift",data.getGift());
        getWritableDatabase().update(TABLE_NAME,cv,whereBuffer.toString(),null);
    }

    /**
     * 删除数据
     * @param data 源数据
     */
    public void Delete(Data data){
        StringBuffer whereBuffer = new StringBuffer();
        whereBuffer.append("_id").append(" = ").append("'").append(data.getId()).append("'");
        getWritableDatabase().delete(TABLE_NAME,whereBuffer.toString(),null);
    }

    /**
     * 通过name 搜索id
     * @param name 名字
     * @return 存在返回id 否则-1
     */
    private int Select_id(String name){
        int id=-1;
        StringBuffer whereBuffer = new StringBuffer();
        whereBuffer.append("name").append(" = ").append("'").append(name).append("'");
        String[] columns = {"_id"};
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, columns, whereBuffer.toString(), null, null, null, null);
        if (cursor.moveToNext()){
            id=cursor.getInt(cursor.getColumnIndex("_id"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return id;
    }

    /**
     * 通过id搜索数据
     * @param id
     * @return 对应数据
     */
    private Data Select(int id){
        Data data=new Data();
        data.setId(id);
        StringBuffer whereBuffer = new StringBuffer();
        whereBuffer.append("_id").append(" = ").append("'").append(id).append("'");
        String[] columns = {"name","birth","gift"};
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, columns, whereBuffer.toString(), null, null, null, null);
        if (cursor.moveToNext()) {
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setBirth(cursor.getString(cursor.getColumnIndex("birth")));
            data.setGift(cursor.getString(cursor.getColumnIndex("gift")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return data;
    }

    /**
     * 全数据搜索
     * @return 全数据
     */
    public ArrayList<Data> Select_ALL(){
        ArrayList<Data>datas=new ArrayList<>();
        ArrayList<Integer>index=new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(TABLE_NAME, new String[]{"_id"}, null, null, null, null,"_id");
        while (cursor.moveToNext()) {//先搜索ID再通过ID 索引全部数据，因为有2M的上限
            index.add(cursor.getInt(cursor.getColumnIndex("_id")));
        }
        cursor.close();
        for(int i=0;i<index.size();i++){
            datas.add(Select(index.get(i)));
        }
        return datas;
    }

    //检测表是否存在
    public boolean tabIsExist(String tabName){
        boolean result = false;
        if(tabName == null){
            return false;
        }
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
            cursor = getReadableDatabase().rawQuery(sql, null);
            if(cursor.moveToNext()){
                int count = cursor.getInt(0);
                if(count>0){
                    result = true;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

    //删除表
    public void Drop_Tab(String tabName){
        getWritableDatabase().execSQL("drop table "+tabName);
    }
    //清空表
    public void Delete_Tab(String tabName){
        getWritableDatabase().execSQL("delete from "+tabName);
    }
}
