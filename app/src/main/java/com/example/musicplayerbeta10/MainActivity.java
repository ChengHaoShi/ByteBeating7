package com.example.musicplayerbeta10;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.musicplayerbeta10.adapter.MusicPagerAdapter;
import com.example.musicplayerbeta10.fragment.MusicFragment;
import com.example.musicplayerbeta10.fragment.VideoFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.musicplayerbeta10.R.drawable.back;

//实现OnClickListener的接口
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //定义activity_main.xml的控件对象
    private ImageView musicTv;
    private ImageView videoTv;
    private ViewPager viewPager;

    //将Fragment放入List集合中，存放fragment对象
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定id
        bangdingID();
        //设置监听
        jianting();
        //创建fragment对象
        MusicFragment musicFragment = new MusicFragment();
        VideoFragment videoFragment = new VideoFragment();
        //将fragment对象添加到fragmentList中
        fragmentList.add(musicFragment);
        fragmentList.add(videoFragment);
        //通过MusicPagerAdapter类创建musicPagerAdapter的适配器，下面我将添加MusicPagerAdapter类的创建方法
        MusicPagerAdapter musicPagerAdapter = new MusicPagerAdapter(getSupportFragmentManager(), fragmentList);
        //viewPager绑定适配器
        viewPager.getBackground().mutate().setAlpha(225);
        viewPager.setAdapter(musicPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        viewPager.setBackgroundResource(back);
                        musicTv.setImageDrawable(getResources().getDrawable(R.mipmap.music2));
                        videoTv.setImageDrawable(getResources().getDrawable(R.mipmap.video));
                        break;
                    case 1:
                        viewPager.setBackgroundResource(R.drawable.back1);
                        musicTv.setImageDrawable(getResources().getDrawable(R.mipmap.music));
                        videoTv.setImageDrawable(getResources().getDrawable(R.mipmap.video2));
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void jianting() {
        musicTv.setOnClickListener(this);
        videoTv.setOnClickListener(this);
    }

    @SuppressLint("WrongViewCast")
    private void bangdingID() {
        musicTv = findViewById(R.id.main_music_tv);
        videoTv = findViewById(R.id.main_video_tv);
        viewPager = findViewById(R.id.main_vp);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_music_tv:
                //实现点击TextView切换fragment
                viewPager.setCurrentItem(0);
                break;
            case R.id.main_video_tv:
                viewPager.setCurrentItem(1);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            final View view = LayoutInflater.from(this).inflate(R.layout.dialog1_layout, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("退出提示");
            builder.setView(view);
            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "回到播放器", Toast.LENGTH_SHORT).show();

                }
            });
            builder.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}

