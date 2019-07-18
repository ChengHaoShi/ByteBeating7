package com.example.musicplayerbeta10.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayerbeta10.R;
import com.example.musicplayerbeta10.entity.Video;
import com.example.musicplayerbeta10.utils.Common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//继承BaseAdapter 实现自定义适配器，复写BaseAdapter的代码
public class VideoAdapter extends BaseAdapter {
    //定义两个属性，用List集合存放Music类
    private Context context;
    private List<Video> videoList;

    //创建MusicAdapter的构造方法，在LogicFragment需要调用MusicAdapter的构造方法来创建适配器
    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;

    }

    //这里需要返回musicList.size()
    @Override
    public int getCount() {
        return Common.videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //在getView（）方法中是实现对模板的绑定，赋值
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //声明View和ViewHolder的对象
        View view = null;
        ViewHolder viewHolder = null;
        //缓存原理，程序运行到这里判断convertView是否为空
        if (convertView == null) {
            //绑定行布局文件，就是绑定我们需要适配的模板
            view = LayoutInflater.from(context).inflate(R.layout.video_item, null);
            //实例化ViewHolder
            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.videoitem_name_tv);
            viewHolder.duration = view.findViewById(R.id.videoitem_duration_tv);
            viewHolder.size = view.findViewById(R.id.videoitem_size_tv);
            viewHolder.thumbnail = view.findViewById(R.id.videoitem_thumbnail_imgv);
            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        //赋值 准确的是绑定赋值的中介
        viewHolder.name.setText(Common.videoList.get(position).name);
        viewHolder.duration.setText(formatTime(Common.videoList.get(position).duration));
        viewHolder.size.setText(formatFileSize(Common.videoList.get(position).size));
        viewHolder.thumbnail.setImageBitmap(Common.videoList.get(position).thumbnail);
        return view;
    }

    //创建一个类ViewHolder，用来存放music_item.xml中的控件
    class ViewHolder {
        TextView name;
        TextView duration;
        TextView size;
        ImageView thumbnail;
    }

    //格式化时间函数
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }

    //格式化文件大小函数
    private String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3) + "GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + "MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";

        return result;
    }
}
