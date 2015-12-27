package com.example.tengyu.lifeholder.tomato;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tengyu on 2015/12/18.
 */


public class tomatoIO {
    private List<tomatoTask> tomatoList;
    private SharedPreferences tomatoSave;
    private boolean autoSave;

    public tomatoIO(SharedPreferences tomatoSave){
        this.tomatoList = new ArrayList<tomatoTask>();
        this.tomatoSave = tomatoSave;
        this.autoSave = false;
    }
    /*
    public void start(){
        SharedPreferences.Editor editor  = tomatoSave.edit();
        editor.putBoolean("TASK_COUNT", true);
        editor.commit();
    }

    public boolean check(){
        return tomatoSave.getBoolean("TASK_COUNT",false);
    }*/

    public void flush(){
        tomatoList.clear();
        int i;
        for(i = 0; i < tomatoSave.getInt("TASK_SIZE", 0);i++){
            tomatoTask tp = new tomatoTask(
                    tomatoSave.getString("title_"+String.valueOf(i),"Empty Task"),
                    tomatoSave.getString("tag_"+String.valueOf(i),"Undefined"),
                    tomatoSave.getInt("tomato_"+String.valueOf(i),0),
                    tomatoSave.getInt("lev_"+String.valueOf(i),2),
                    tomatoSave.getBoolean("ifRemind_"+String.valueOf(i),false),
                    new Date(tomatoSave.getLong("time_"+String.valueOf(i),new Date().getTime())),
                    new Date(tomatoSave.getLong("deadline_"+String.valueOf(i),new Date().getTime()))
            );
            tomatoList.add(tp);
        }
        if(i==0)
            testTomatoes();
    }

    public void swap(int s,int t){
        tomatoTask tp = tomatoList.get(s);
        tomatoList.set(s,tomatoList.get(t));
        tomatoList.set(t,tp);
    }

    public void sort(int s,int t){
        int i,j;
        if(s<t){
            i = s;
            j = t+1;
            while(true){
                do i++;
                while(!(larger(tomatoList.get(s),tomatoList.get(i))||i==t));
                do j--;
                while(!(larger(tomatoList.get(j),tomatoList.get(s))||j==s));
                if(i<j)
                    swap(i,j);
                else
                    break;
            }
            swap(s,j);
            sort(s, j - 1);
            sort(j+1,t);
        }
    }

    public void insert(tomatoTask tp){
        int i;
        for(i = 0; i < tomatoList.size(); i++){
            if(larger(tp,tomatoList.get(i))) {
                tomatoList.add(i, tp);
                return;
            }
        }
        tomatoList.add(i, tp);
    }

    public void commit(int i, tomatoTask tp){
        SharedPreferences.Editor editor = tomatoSave.edit();
        if(autoSave)
            editor.putInt("TASK_SIZE",tomatoList.size());
        editor.putString("title_"+String.valueOf(i),tp.getTitle());
        editor.putString("tag_"+String.valueOf(i),tp.getTag());
        editor.putInt("tomato_"+String.valueOf(i),tp.getTomato());
        editor.putInt("lev_"+String.valueOf(i),tp.getLev());
        editor.putBoolean("ifRemind_"+String.valueOf(i),tp.IfRemind());
        editor.putLong("time_" + String.valueOf(i), tp.getDate().getTime());
        editor.putLong("deadline_" + String.valueOf(i), tp.getDeadline().getTime());
        editor.commit();
    }

    public void save(){
        for(int i = 0; i < tomatoList.size(); i++){
                commit(i, tomatoList.get(i));
        }
        SharedPreferences.Editor editor = tomatoSave.edit();
        editor.putInt("TASK_SIZE", tomatoList.size());
        editor.commit();
        this.sort(0,tomatoList.size()-1);
    }

    public tomatoTask get(int position){
        if(position<tomatoList.size()&&position>=0)
            return tomatoList.get(position);
        else
            return new tomatoTask();
    }

    public void set(int position,tomatoTask tp){
        remove(position);
        insert(tp);
    }

    public void remove(int position){
        if(position<tomatoList.size()&&position>=0)
            tomatoList.remove(position);
    }

    public List<tomatoTask> getList(){
        return tomatoList;
    }

    public void tomato(String title){
        int position = -1;
        for(int i = 0;i<tomatoList.size();i++){
            if(tomatoList.get(i).getTitle().equals(title)){
                position = i;
                break;
            }
        }
        if(position<tomatoList.size()&&position>=0) {
            if (tomatoList.get(position).tomato()) {
                tomatoList.set(position, tomatoList.get(position));
            }
        }
        //tomatoSave.edit().putBoolean("TASK_COUNT",false);
        //tomatoSave.edit().commit();
        //return false;
    }

    private boolean larger(tomatoTask t1, tomatoTask t2){
        int lev1 = t1.getLev();
        int lev2 = t2.getLev();
        int tom1 = t1.getTomato();
        int tom2 = t2.getTomato();
        boolean f1 = t1.repOK();
        boolean f2 = t2.repOK();
        if(f1&&f2){
            if(lev1>lev2)
                return true;
            else if(lev1==lev2 && t1.getDeadline().getTime()<t2.getDeadline().getTime())
                return true;
        }
        else if(!f2){
            if(f1)
                return true;
            else if(tom1==0){
                if(tom2!=0)
                    return true;
                else if(t1.getDate().getTime()>t2.getDate().getTime())
                    return true;
            }
            else if(tom2!=0) {
                if (t1.getDate().getTime() > t2.getDate().getTime())
                    return true;
            }
        }
        return false;
    }

    public void testTomatoes(){
        tomatoList.clear();
        tomatoTask tomato1 = new tomatoTask("点击右下角创建任务！",0,new Date(),1,false);
        insert(tomato1);
        /*
        tomatoTask tomato2 = new tomatoTask("安卓界面原型设计",5,new Date(2015-1900,11,20),2,false);
        insert(tomato2);
        tomatoTask tomato3 = new tomatoTask("数据库用户级约束",4,new Date(2015-1900,11,23),1);
        insert(tomato3);
        tomatoTask tomato4 = new tomatoTask("数学建模工作",6,new Date(2015-1900,11,25),2);
        insert(tomato4);
        tomatoTask tomato5 = new tomatoTask("锻炼身体",2,new Date(2036-1900,11,31),1);
        insert(tomato5);
        tomatoTask tomato6 = new tomatoTask("用户界面设计",6,new Date(2015-1900,11,19));
        insert(tomato6);
        tomatoTask tomato7 = new tomatoTask("图标修正",1,new Date(2015-1900,11,18),3);
        insert(tomato7);
        tomatoTask tomato8 = new tomatoTask("视频剪辑",0,new Date(2016-1900,2,1),3);
        insert(tomato8);
        tomatoTask tomato9 = new tomatoTask("字幕处理",1,new Date(2016-1900,1,22));
        insert(tomato9);
        tomatoTask tomato10 = new tomatoTask("冯如杯", 7, new Date(2016-1900,3,1));
        insert(tomato10);
        tomatoTask tomato11 = new tomatoTask("名字要长长长长长长长长长长长长长", 6, new Date(2016-1900,3,2));
        insert(tomato11);
        */
        Log.d("tomatoTaskInit", "Success");
    }
}
