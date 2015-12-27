package com.example.tengyu.lifeholder.tomato;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.example.tengyu.lifeholder.R;
import com.fourmob.datetimepicker.date.SimpleMonthAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
/**
 * Created by tengyu on 2015/12/18.
 */
public class tomatoTaskAdapter extends ArrayAdapter<tomatoTask> {
    private int resourceId;

    public tomatoTaskAdapter(Context context, int textViewResourceId,List<tomatoTask> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        tomatoTask tomato = getItem(position);
        View view;
        ViewHolder viewholder;
        DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DEFAULT, Locale.ENGLISH);

        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewholder = new ViewHolder();
            //查找每个ViewItem中,各个子View,放进holder中
            viewholder.titles = (TextView) view.findViewById(R.id.tomatotask_title);
            viewholder.subtitles = (TextView) view.findViewById(R.id.tomatotask_subtitle);
            viewholder.leftTime = (TextView) view.findViewById(R.id.tomatotask_leftTime);
            viewholder.tomato = (ImageView) view.findViewById(R.id.tomatotask_image);
            //保存对每个显示的ViewItem中, 各个子View的引用对象
            view.setTag(viewholder);
            Log.d("tomatoTaskAdapter", "InitViewHolderSuccess");
        }
        else// I think this a bug, program can not run here!!!--linc2014.11.12
        {
            view = convertView;
            viewholder = (ViewHolder)view.getTag();
        }


        //获取当前要显示的数据
        viewholder.titles.setText(tomato.getTitle());
        viewholder.subtitles.setText(dateFormat.format(tomato.getDate()));
        long time = (tomato.getDeadline().getTime()-(new Date()).getTime())/60000;

        if(time>=0){
            if(tomato.getTomato()==0)
                viewholder.leftTime.setText("success");
            else if(time<60){
                if(time==0)
                    viewholder.leftTime.setText("< 1 minute");
                else
                    viewholder.leftTime.setText("< "+(time+1)+" minutes");
            }
            else if(time<1440){
                 viewholder.leftTime.setText("< "+(time/60+1)+" hours");
            }
            else if(time<44640){
                viewholder.leftTime.setText("< "+(time/1440+1)+" days");
            }
            else if(time<527040){
                viewholder.leftTime.setText("< "+(time/44640+1)+" months");
            }
            else
                viewholder.leftTime.setText("> 1 year");
        }
        else{
            if(tomato.getTomato()==0)
                viewholder.leftTime.setText("success");
            else
                viewholder.leftTime.setText("failed");
        }
       // Log.d("SetTextView", tomato.getTitle());
        switch (tomato.getTomato()) {
            case 1:
                viewholder.tomato.setImageResource(R.drawable.stomato_1);
                break;
            case 2:
                viewholder.tomato.setImageResource(R.drawable.stomato_2);
                break;
            case 3:
                viewholder.tomato.setImageResource(R.drawable.stomato_3);
                break;
            case 4:
                viewholder.tomato.setImageResource(R.drawable.stomato_4);
                break;
            case 5:
                viewholder.tomato.setImageResource(R.drawable.stomato_5);
                break;
            case 6:
                viewholder.tomato.setImageResource(R.drawable.stomato_6);
                break;
            case 7:
                viewholder.tomato.setImageResource(R.drawable.stomato_7);
                break;
            default:
                viewholder.tomato.setImageResource(R.drawable.stomato_1);
                break;
        }
        return view;
    }

    private static class ViewHolder
    {
        TextView titles;
        TextView subtitles;
        TextView leftTime;
        ImageView tomato;
    }
}
