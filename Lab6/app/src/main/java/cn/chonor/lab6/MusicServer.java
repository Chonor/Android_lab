package cn.chonor.lab6;


import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.IOException;
/**
 * Created by Chonor on 2017/11/14.
 */

public class MusicServer{
    private static MediaPlayer mediaPlayer=new MediaPlayer();

    public  MusicServer() {
        try{
            mediaPlayer.setDataSource("/data/K.Will-Melt.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Start_Pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
        }
    }
    public void Stop_Play(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            try{
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    protected class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data,Parcel reply,int flags)throws RemoteException{
            switch (code){
                case 101:
                    break;
                case 102:
                    break;
                case 103:
                    break;
                case 104:
                    break;
                case 105:
                    break;
            }
            return super.onTransact(code,data,reply,flags);
        }
    }


}
