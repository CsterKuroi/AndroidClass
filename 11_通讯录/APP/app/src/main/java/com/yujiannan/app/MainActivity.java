package com.yujiannan.app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.meg7.widget.CircleImageView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.umeng.update.UmengUpdateAgent;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    public static int no = 1;
    TextView textView = null;
    public DBOpenHandler dbOpenHandler = new DBOpenHandler(MainActivity.this,"db.db",null,1);

    final static String NO_PEOPLE = "NO PEOPLE";




    public SQLiteDatabase db;

    ResideMenu resideMenu;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbOpenHandler = new DBOpenHandler(this,"db.db",null,1);

       final com.meg7.widget.CircleImageView icon = new CircleImageView(this);
        icon.setImageResource(R.drawable.father);
        db=dbOpenHandler.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from message where no=?",new String[]{String.valueOf(no)});
        if (cursor.moveToFirst()){
            String temp=cursor.getString(cursor.getColumnIndex("picture"));
            if (temp != "")
                icon.setImageURI(Uri.parse(temp));
            else
                icon.setImageResource(R.drawable.icon_no);
        }
        cursor.close();
        db.close();
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        db = dbOpenHandler.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM message WHERE no=?", new String[]{String.valueOf(MainActivity.no)});
        String pic = "";
        if (cursor.moveToFirst()) {
            pic = cursor.getString(cursor.getColumnIndex("picture"));
        }
        db.close();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setTheme(3);
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.icon_osister);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon1).build();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("Osister",icon);
                Toast.makeText(MainActivity.this,"1",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.icon_mother);
        SubActionButton button2 = itemBuilder.setContentView(itemIcon2).build();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("mother",icon);
                Toast.makeText(MainActivity.this,"2",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon3 = new ImageView(this);
        itemIcon3.setImageResource(R.drawable.icon_father);
        SubActionButton button3 = itemBuilder.setContentView(itemIcon3).build();
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("father",icon);
                Toast.makeText(MainActivity.this,"3",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon4 = new ImageView(this);
        itemIcon4.setImageResource(R.drawable.icon_obrbother);
        SubActionButton button4 = itemBuilder.setContentView(itemIcon4).build();
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("Obrother",icon);
                Toast.makeText(MainActivity.this,"4",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon5 = new ImageView(this);
        itemIcon5.setImageResource(R.drawable.icon_partner);
        SubActionButton button5 = itemBuilder.setContentView(itemIcon5).build();
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("partner",icon);
                Toast.makeText(MainActivity.this,"5",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon6 = new ImageView(this);
        itemIcon6.setImageResource(R.drawable.icon_ybrother);
        SubActionButton button6 = itemBuilder.setContentView(itemIcon6).build();
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("Ybrother",icon);
                Toast.makeText(MainActivity.this,"6",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon7 = new ImageView(this);
        itemIcon7.setImageResource(R.drawable.icon_son);
        SubActionButton button7 = itemBuilder.setContentView(itemIcon7).build();
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("son",icon);
                Toast.makeText(MainActivity.this,"7",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon8 = new ImageView(this);
        itemIcon8.setImageResource(R.drawable.icon_daughter);
        SubActionButton button8 = itemBuilder.setContentView(itemIcon8).build();
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("daughter",icon);
                Toast.makeText(MainActivity.this,"8",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon9 = new ImageView(this);
        itemIcon9.setImageResource(R.drawable.icon_ysister);
        SubActionButton button9 = itemBuilder.setContentView(itemIcon9).build();
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change("Ysister",icon);
                Toast.makeText(MainActivity.this,"9",Toast.LENGTH_SHORT).show();
            }
        });
        ImageView itemIcon10 = new ImageView(this);
        itemIcon10.setImageResource(R.drawable.img_call);
        SubActionButton button10 = itemBuilder.setContentView(itemIcon10).build();
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"10",Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button10)
                .addSubActionView(button9)
                .addSubActionView(button8)
                .addSubActionView(button7)
                .addSubActionView(button6)
                .addSubActionView(button5)
                .addSubActionView(button4)
                .addSubActionView(button3)
                .addSubActionView(button2)
                .addSubActionView(button1)
                .attachTo(actionButton)
                .build();


        // attach to current activity;
        resideMenu= new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = { "Home", "检查更新", "Calendar", "Settings" };
        int menu_icon[] = { R.drawable.img_call, R.drawable.img_call, R.drawable.img_call, R.drawable.img_call };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, menu_icon[i], titles[i]);
            resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
            if (i == 0)
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this,AboutAuthorActivity.class);
                        startActivity(intent);
                    }
                });
            else if (i == 1){
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UmengUpdateAgent.update(MainActivity.this);
                    }
                });
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void change(String name,com.meg7.widget.CircleImageView icon) {

        db = dbOpenHandler.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM message WHERE no=?",new String[]{String.valueOf(no)});
        int temp = 0;
        if (cursor.moveToFirst()) {
            temp = cursor.getInt(cursor.getColumnIndex(name));
        }
        cursor.close();
        if (temp == 0){
            Toast.makeText(MainActivity.this,NO_PEOPLE,Toast.LENGTH_SHORT).show();
            icon.setImageResource(R.drawable.icon_no);
        }
        else{
            no = temp;
            cursor = db.rawQuery("SELECT * FROM message WHERE no=?", new String[]{String.valueOf(no)});
            if (cursor.moveToFirst()){


                if (cursor.moveToFirst()){
                    String t=cursor.getString(cursor.getColumnIndex("picture"));
                    if (t != "")
                        icon.setImageURI(Uri.parse(t));
                }


            }
            cursor.close();
            db.close();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
            case 1:
        }
    }
}
