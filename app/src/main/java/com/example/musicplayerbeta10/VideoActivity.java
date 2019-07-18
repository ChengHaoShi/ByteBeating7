package com.example.musicplayerbeta10;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.musicplayerbeta10.utils.Common;

import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";
    private VideoView videoView;
    private ImageView videolight_imagv;
    private ImageView videovolumn_imagv;
    private int position;
    private int k;
    private Vibrator vibrator;
    private GestureDetector mGestureDetector;

    @Override
    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
        position = intent.getIntExtra("position", 0);            //获取position
        Uri uri = Uri.parse(Common.videoList.get(position).path);

        videoView = (VideoView)this.findViewById(R.id.video_videoView );

        videolight_imagv = (ImageView)this.findViewById(R.id.videolight_imagv);
        videolight_imagv.setVisibility(View.INVISIBLE);

        videovolumn_imagv = (ImageView)this.findViewById(R.id.videovolume_imagv);
        videovolumn_imagv.setVisibility(View.INVISIBLE);

        //设置视频控制器
        videoView.setMediaController(new MediaController(this));

        //设置视频路径
        videoView.setVideoURI(uri);



        //开始播放视频
        videoView.start();
    }
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // if (lp.screenBrightness <= 0.1) {
        // return;
        // }
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
//            vibrator.vibrate(pattern, -1);
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
//            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
//            vibrator.vibrate(pattern, -1);
        }
        Log.e(TAG, "lp.screenBrightness= " + lp.screenBrightness);
        getWindow().setAttributes(lp);
        if (lp.screenBrightness * 100 >= 90) {
            videolight_imagv.setImageResource(R.drawable.light_100);
        } else if (lp.screenBrightness * 100 >= 80 && lp.screenBrightness * 100 < 90) {
            videolight_imagv.setImageResource(R.drawable.light_90);
        } else if (lp.screenBrightness * 100 >= 70 && lp.screenBrightness * 100 < 80) {
            videolight_imagv.setImageResource(R.drawable.light_80);
        } else if (lp.screenBrightness * 100 >= 60 && lp.screenBrightness * 100 < 70) {
            videolight_imagv.setImageResource(R.drawable.light_70);
        } else if (lp.screenBrightness * 100 >= 50 && lp.screenBrightness * 100 < 60) {
            videolight_imagv.setImageResource(R.drawable.light_60);
        } else if (lp.screenBrightness * 100 >= 40 && lp.screenBrightness * 100 < 50) {
            videolight_imagv.setImageResource(R.drawable.light_50);
        } else if (lp.screenBrightness * 100 >= 30 && lp.screenBrightness * 100 < 40) {
            videolight_imagv.setImageResource(R.drawable.light_40);
        } else if (lp.screenBrightness * 100 >= 20 && lp.screenBrightness * 100 < 20) {
            videolight_imagv.setImageResource(R.drawable.light_30);
        } else if (lp.screenBrightness * 100 >= 10 && lp.screenBrightness * 100 < 20) {
            videolight_imagv.setImageResource(R.drawable.light_20);
        }

        videolight_imagv.setVisibility(View.VISIBLE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                videolight_imagv.setVisibility(View.INVISIBLE);
                //do something
            }
        },1000);//延时1s执行

    }


    public void setAudio(int volume) {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        //当前音量
        k = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //最大音量
        int max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        Log.d("==d==", "" + max);
        Log.d("==d==", "" + k);
        k = k + volume;
        if (k >= 0 && k <= max) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, k, AudioManager.FLAG_PLAY_SOUND);
        } else {
            return;
        }

        if (k >= 10) {
            videovolumn_imagv.setImageResource(R.drawable.volmn_100);
        } else if (k >= 5 && k < 10) {
            videovolumn_imagv.setImageResource(R.drawable.volmn_60);
        } else if (k > 0 && k < 5) {
            videovolumn_imagv.setImageResource(R.drawable.volmn_30);
        } else {
            videovolumn_imagv.setImageResource(R.drawable.volmn_no);
        }

        videovolumn_imagv.setVisibility(View.VISIBLE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                videovolumn_imagv.setVisibility(View.INVISIBLE);
                //do something
            }
        },1000);//延时1s执行
    }

    @Override
    protected void onResume() {
        mGestureDetector = new GestureDetector(
                new GestureDetector.OnGestureListener() {
                    public boolean onSingleTapUp(MotionEvent e) {
                        return false;
                    }
                    public boolean onDown(MotionEvent e) {
                        return false;
                    }
                    public void onLongPress(MotionEvent e) {
                    }
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        return true;
                    }
                    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                            float distanceX, float distanceY) {
                        final double FLING_MIN_DISTANCE = 0.5;
                        final double FLING_MIN_VELOCITY = 0.5;
                        if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                                && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                            Log.e(TAG, "up");
                            if(e1.getX()<500){
                                setBrightness(4);}
                            else{
                                setAudio(1);}
                        }
                        if (e1.getY() - e2.getY() < FLING_MIN_DISTANCE
                                && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                            Log.e(TAG, "down");
                            if(e1.getX()<500){
                            setBrightness(-4);}
                            else{
                                setAudio(-1);
                            }
                        }
                        return true;
                    }


                    public void onShowPress(MotionEvent e) {
                        // TODO Auto-generated method stub
                    }
                });
        super.onResume();
    }
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // getVideoInfosfromPath(filePath);
            }
            result = super.onTouchEvent(event);
        }
        return result;
    }
    @Override
    protected void onStop() {
        if (null != vibrator) {
            vibrator.cancel();
        }
        super.onStop();
    }

}

