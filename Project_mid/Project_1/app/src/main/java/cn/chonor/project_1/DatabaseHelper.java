package cn.chonor.project_1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;

/**
 * Created by Chonor on 2017/11/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String name = "INFO.db";
    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS role "
                +"(roleid integer primary key autoincrement, "
                + "name varchar(40), "
                + "place varchar(1000), "
                + "info varchar(3000), "
                + "sex INTEGER, "
                + "both INTEGER, "
                + "dead INTEGER, "
                + "image BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void Insert(Data data){
        ContentValues cv=new ContentValues();
        cv.put("name",data.getName());
        cv.put("place",data.getPlace());
        cv.put("info",data.getInfo());
        cv.put("sex",data.getSex());
        cv.put("both",data.getBoth());
        cv.put("dead",data.getDead());
        cv.put("image",data.getbyte());
        getWritableDatabase().insert("role",null,cv);
    }

    public void Updatas(Data data){
        StringBuffer whereBuffer = new StringBuffer();
        //whereBuffer.append("roleid").append(" = ").append("'").append(data.getId()).append("'");
        ContentValues cv=new ContentValues();
        cv.put("name",data.getName());
        cv.put("place",data.getPlace());
        cv.put("info",data.getInfo());
        cv.put("sex",data.getSex());
        cv.put("both",data.getBoth());
        cv.put("dead",data.getDead());
        cv.put("image",data.getbyte());
        getWritableDatabase().update("role",cv,whereBuffer.toString(),null);
    }
    public void Delete(Data data){
        StringBuffer whereBuffer = new StringBuffer();
        //whereBuffer.append("roleid").append(" = ").append("'").append(data.getId()).append("'");
        getWritableDatabase().delete("role",whereBuffer.toString(),null);
    }
    public Data Select(int id){
        Data data=new Data();
        //data.setId(id);
        StringBuffer whereBuffer = new StringBuffer();
        whereBuffer.append("roleid").append(" = ").append("'").append(id).append("'");
        String[] columns = {"name","place","info","sex","both","dead","image"};
        Cursor cursor = getReadableDatabase().query("role", columns, whereBuffer.toString(), null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            data.setName(cursor.getString(cursor.getColumnIndex("name")));
            data.setPlace(cursor.getString(cursor.getColumnIndex("place")));
            data.setInfo(cursor.getString(cursor.getColumnIndex("info")));
            data.setSex(cursor.getInt(cursor.getColumnIndex("sex")));
            data.setBoth_and_Dead(cursor.getInt(cursor.getColumnIndex("both")),cursor.getInt(cursor.getColumnIndex("dead")));
            data.setbyte(cursor.getBlob(cursor.getColumnIndex("image")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return data;
    }
    public boolean tableIsExist(){
        boolean result = false;
        Cursor cursor = null;
        try {
            String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"+"role".trim()+"' ";
            cursor = getWritableDatabase().rawQuery(sql, null);
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
}
