package cn.chonor.lab6;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static MediaPlayer mediaPlayer=new MediaPlayer();
    private TextView status,begin,end;
    private Button play,stop,quit;
    private ImageView imageView;
    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Init_listener();
    }

    private void Init(){
        status=(TextView)findViewById(R.id.status);
        begin=(TextView)findViewById(R.id.begin);
        end=(TextView)findViewById(R.id.end);
        play=(Button)findViewById(R.id.Play);
        stop=(Button)findViewById(R.id.Stop);
        quit=(Button)findViewById(R.id.Quit);
        imageView=(ImageView)findViewById(R.id.imageView);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        mediaPlayer=MediaPlayer.create(this,R.raw.melt);
        try{
            //mediaPlayer.setDataSource("/data/K.Will-Melt.mp3");
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        }catch (Exception e) {
            e.printStackTrace();
        }

        int len=mediaPlayer.getDuration();
        seekBar.setMax(len);
        len=len/1000;
        end.setText(String.valueOf(len/60)+":"+String.valueOf(len%60));
        begin.setText("0:00");
        status.setText("ready");
    }
    private void Init_listener(){
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(play.getText().toString().equals("PLAY")) {
                    play.setText("PAUSE");
                    status.setText("play");
                }
                else {
                    play.setText("PLAY");
                    status.setText("pause");
                }
                Start_Pause();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop_Play();
                status.setText("ready");
                begin.setText("0:00");
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    public void Start_Pause(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else{
            mediaPlayer.start();
            Toast.makeText(MainActivity.this,"kaishi",Toast.LENGTH_SHORT).show();
        }
    }
    public void Stop_Play(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer=MediaPlayer.create(this,R.raw.melt);
            try{
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
