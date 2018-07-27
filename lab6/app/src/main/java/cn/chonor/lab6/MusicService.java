package cn.chonor.lab6;


import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chonor on 2017/11/14.
 */

public class MusicService extends Service {
    private ArrayList<String>PATHS=new ArrayList<>();
    private ArrayList<String>Music_name=new ArrayList<>();
    private ArrayList<String>Music_id=new ArrayList<>();
    private ArrayList<String>Music_albumid=new ArrayList<>();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private MyBinder myBinder = new MyBinder();

    public MusicService() {

    }
    private void FindPath() {
        final File music = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        music.listFiles();
        for (File item : music.listFiles()) {
            PATHS.add(item.toString());
        }
    }

    /**
     * 获取媒体数据库
     */
    public void scanAllAudioFiles(){
        //查询媒体数据库
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        //遍历媒体数据库
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //歌曲编号
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲名
                String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //歌曲专辑编号
                int album_id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
                String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
                Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                if (size > 1024 * 800) {//如果文件大小大于800K，将该文件信息存入
                    PATHS.add(url);
                    Music_name.add(tilte);
                    Music_id.add(String.valueOf(id));
                    Music_albumid.add(String.valueOf(album_id));
                }
                cursor.moveToNext();
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    protected class MyBinder extends Binder {
        /**
         * 改变播放状态 暂停和播放互相转换
         */
        public void Start_Pause() {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.start();
            }
        }

        /**
         * 停止当前播放，重新使得mediaPlayer回到载入文件之后的初始状态
         */
        public void Stop_Play() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 歌曲库初始化，默认先使用第一首歌曲
         */
        public void Music_init() {
            scanAllAudioFiles();
            try {
                mediaPlayer.setDataSource(PATHS.get(0));
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 载入新的歌曲
         * @param pos
         */
        private void Music_reInit(int pos){
            if (mediaPlayer != null) {
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(PATHS.get(pos));
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                    mediaPlayer.setLooping(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    Start_Pause(); //播放or暂停
                    break;
                case 102:
                    Stop_Play();//停止
                    break;
                case 103:
                    mediaPlayer.release();//退出
                    break;
                case 104:
                    reply.writeInt(mediaPlayer.getCurrentPosition());//更新进度条后调整音乐
                    break;
                case 105:
                    mediaPlayer.seekTo(data.readInt());//滑动进度条更新播放位置
                    break;
                case 106: {
                    Music_reInit(data.readInt());
                    reply.writeInt(mediaPlayer.getDuration());
                    break;
                }
                default: {//其他code设置 为初始化并回传数据
                    Music_init();
                    reply.writeInt(mediaPlayer.getDuration());
                    reply.writeStringList(Music_name);
                    reply.writeStringList(Music_id);
                    reply.writeStringList(Music_albumid);
                    break;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
