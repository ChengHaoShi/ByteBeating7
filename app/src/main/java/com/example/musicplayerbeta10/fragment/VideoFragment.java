package com.example.musicplayerbeta10.fragment;


import android.Manifest;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicplayerbeta10.R;
import com.example.musicplayerbeta10.VideoActivity;
import com.example.musicplayerbeta10.adapter.VideoAdapter;
import com.example.musicplayerbeta10.entity.Video;
import com.example.musicplayerbeta10.utils.Common;
import com.master.permissionhelper.PermissionHelper;

import static com.example.musicplayerbeta10.utils.Common.videoList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    private String TAG = "HelloActivity";                                       //下面两个属性和获取mediadatabase的权限有关系，可查阅代码块下的链接
    private PermissionHelper permissionHelper;
    private ListView listView;
    private VideoAdapter adapter;

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video, container, false);

        permissionHelper = new PermissionHelper(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        permissionHelper.request(new PermissionHelper.PermissionCallback() {
            @Override
            public void onPermissionGranted() {
                initListView();                                          //获取权限后扫描数据库获取信息
                Log.d(TAG, "onPermissionGranted() called");
            }

            @Override
            public void onIndividualPermissionGranted(String[] grantedPermission) {
                Log.d(TAG, "onIndividualPermissionGranted() called with: grantedPermission = [" + TextUtils.join(",", grantedPermission) + "]");
            }

            @Override
            public void onPermissionDenied() {
                Log.d(TAG, "onPermissionDenied() called");
            }

            @Override
            public void onPermissionDeniedBySystem() {
                Log.d(TAG, "onPermissionDeniedBySystem() called");
            }
        });
// 权限代码结束
        //对Listview进行监听
        listView = view.findViewById(R.id.video_lv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {             //将listView的每一个item实现监听
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Video v : videoList
                ) {
                    v.isPlaying = false;
                }
                videoList.get(position).isPlaying = true;
                //更新界面
                adapter.notifyDataSetChanged();
                //intent实现页面的跳转，getActivity()获取当前的activity， MusicActivity.class将要调转的activity
                Intent intent = new Intent(getActivity(), VideoActivity.class);
                //使用putExtra（）传值
                intent.putExtra("position", position);
                startActivity(intent);

                PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            }
        });
        adapter = new VideoAdapter(getActivity(), videoList);                //创建MusicAdapter的对象，实现自定义适配器的创建
        listView.setAdapter(adapter);                                                 //listView绑定适配器
        return view;
    }

    // 权限代码
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionHelper != null) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    // 权限代码结束

    //nitListView()实现对手机中MediaDataBase的扫描
    private void initListView() {
        Common.videoList.clear();
        //获取ContentResolver的对象，并进行实例化
        ContentResolver resolver = getActivity().getContentResolver();
        //获取游标
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER); //创建游标MediaStore.Audio.Media.EXTERNAL_CONTENT_URI获取音频的文件，后面的是关于select筛选条件，这里填土null就可以了
        //游标归零
        if(cursor.moveToFirst()){
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));// 视频的id
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)); // 视频名称
                String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)); //分辨率
                long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));// 大小
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));// 时长
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));//修改时间
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                Bitmap thumbnail = getVideoBitmap(path); //缩略图
                //String thumbnailspath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                //Bitmap thumbnail = BitmapFactory.decodeFile(thumbnailspath);

                //创建Music对象，并赋值
                Video video = new Video();
                video.id = id;
                video.name = name;
                video.resolution = resolution;
                video.size = size;
                video.duration = duration;
                video.date = date;
                video.path = path;
                //video.thumbnail = thumbnail;
                video.thumbnail = thumbnail;

                //将music放入musicList集合中
                Common.videoList.add(video);
            }  while (cursor.moveToNext());
        }else {
            Toast.makeText(getActivity(), "本地没有视频", Toast.LENGTH_SHORT).show();
        }
        cursor.close();                                                                         //关闭游标
    }


    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }


    public static Bitmap getVideoBitmap(String path) {
        Log.e("Icon", "path:" + path);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path);
            Bitmap frameAtTime = retriever.getFrameAtTime();
            return frameAtTime;
        } catch (Exception e) {
            return null;
        } finally {
            retriever.release();
        }

    }

}
