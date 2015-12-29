/**
 * 
 */
package com.example.keepfit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.MediaController;
//import android.media.session.MediaController;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.VideoView;


import java.io.File;
import java.net.URL;
//Download by http://www.codefans.net
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;



public class VedioPlayActivity extends Activity  {
    private Button buttonstop;  //用于开始和暂停的按钮
    private Button buttonplay;
    private SurfaceView playSurfaceView;   //绘图容器对象，用于把视频显示在屏幕上
    private String url = null;   //视频播放地址
    private MediaPlayer mediaPlayer;    //播放器控件
    private int postSize;    //保存义播视频大小
    private SeekBar seekbar;   //进度条控件
    private boolean flag = true;   //用于判断视频是否在播放中
    private RelativeLayout relativeLayout;
    private boolean display;   //用于是否显示其他按钮

    private View progressBar;   //ProgressBar
    private upDateSeekBar update;   //更新进度条用
    /////////////////////////////////////////
   
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 应用运行时，保持屏幕高亮，不锁屏
        init();  //初始化数据
    }
    private void init() {
        mediaPlayer = new MediaPlayer();   //创建一个播放器对象
        update = new upDateSeekBar();  //创建更新进度条对象
        
        setContentView(R.layout.activity_vedio_play);   //加载布局文件
        
        seekbar = (SeekBar) findViewById(R.id.seekbar);  //进度条
        
        buttonplay = (Button) findViewById(R.id.play);
        buttonplay.setEnabled(false); //刚进来，设置其不可点击
        
        buttonstop = (Button) findViewById(R.id.stop);
        buttonstop.setEnabled(false); //刚进来，设置其不可点击
        
        playSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
  //      playSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //不缓冲
        playSurfaceView.getHolder().setKeepScreenOn(true);   //保持屏幕高亮
        playSurfaceView.getHolder().addCallback(new surFaceView());   //设置监听事件
     
        relativeLayout = (RelativeLayout) findViewById(R.id.rl2);
        progressBar = findViewById(R.id.pb);
        setListener();
    }

    class PlayMovie extends Thread {   //播放视频的线程

        int post = 0;

        public PlayMovie(int post) {
            this.post = post;

        }

        @Override
        public void run() {
            Message message = Message.obtain();
            try {
                Intent intent=getIntent();
                String filename=intent.getStringExtra("videoname");
                url=shangdianActivity.path+"/"+filename;
               // url = filename;
               
                mediaPlayer.setDataSource(url);   //设置播放路径
                
                mediaPlayer.setDisplay(playSurfaceView.getHolder());  //把视频显示在SurfaceView上
                mediaPlayer.setOnPreparedListener(new PreparedListener(post) );  //设置监听事件
                mediaPlayer.prepare();  //准备播放
       
            } catch (Exception e) {
                message.what = 2;
                Log.e("hck", e.toString());
            }


            super.run();
        }
    }

    class PreparedListener implements OnPreparedListener {
        int postSize;

        public PreparedListener(int postSize) {
            this.postSize = postSize;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            buttonstop.setEnabled(true);
            buttonplay.setEnabled(true);  
            display = false;
          /*  if (mediaPlayer != null) {
                mediaPlayer.start();  //开始播放视频
            } else {
                return;
            }      */
            if (postSize > 0) {  //说明中途停止过（activity调用过pase方法，不是用户点击停止按钮），跳到停止时候位置开始播放
                mediaPlayer.seekTo(postSize);   //跳到postSize大小位置处进行播放
            }
            new Thread(update).start();   //启动线程，更新进度条
        }
    }

    
    private class surFaceView implements Callback {     //上面绑定的监听的事件

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {   //创建完成后调用
            if (postSize > 0 && url!= null) {    //说明，停止过activity调用过pase方法，跳到停止位置播放
                new PlayMovie(postSize).start();
                flag = true;
                int sMax = seekbar.getMax();
                int mMax = mediaPlayer.getDuration();
                seekbar.setProgress(postSize * sMax / mMax);
                postSize = 0;
                progressBar.setVisibility(View.GONE);
            }
            else {
                new PlayMovie(0).start();   //表明是第一次开始播放
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) { //activity调用过pase方法，保存当前播放位置
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                postSize = mediaPlayer.getCurrentPosition();
                mediaPlayer.stop();
                flag = false;
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setListener() {
        mediaPlayer
                .setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    }
                });

        mediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //视频播放完成
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        flag = false;
                       // bt.setBackgroundResource(R.drawable.movie_play_bt);
                    }
                });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });
        //////////////////////////////////////////////
/**
 * 如果视频在播放，则调用mediaPlayer.pause();，停止播放视频，反之，mediaPlayer.start()  ，同时换按钮背景
 */
        buttonplay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (! mediaPlayer.isPlaying()) {
                    if (flag == false) {
                        flag = true;
                        new Thread(update).start();
                    }
                    mediaPlayer.start();
                   	progressBar.setVisibility(View.GONE);  //准备完成后，隐藏控件      	
                   	seekbar.setVisibility(View.GONE); 
                   	buttonstop.setVisibility(View.GONE);
                    buttonplay.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });
        buttonstop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    postSize = mediaPlayer.getCurrentPosition();
                }

            }
        });
///////////////////////////////////////////
        /**
         * 更新进度条
         */
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int value = seekbar.getProgress() * mediaPlayer.getDuration()  //计算进度条需要前进的位置数据大小
                        / seekbar.getMax();
                mediaPlayer.seekTo(value);

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

            }
        });
        ////////////////////////////////////////////////
/**
 * 点击屏幕，切换控件的显示，显示则应藏，隐藏，则显示
 */
        playSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (display) {
                    buttonstop.setVisibility(View.GONE);
                    buttonplay.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE); 
                    progressBar.setVisibility(View.GONE);  //准备完成后，隐藏控件      	
                   	seekbar.setVisibility(View.GONE); 
                    display = false;
                } else {
                	relativeLayout.setVisibility(View.VISIBLE);
                    buttonstop.setVisibility(View.VISIBLE);
                    buttonplay.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);  //准备完成后，隐藏控件      	
                   	seekbar.setVisibility(View.VISIBLE); 
                    playSurfaceView.setVisibility(View.VISIBLE);
                    /**
                     * 设置播放为全屏
                     */
                    ViewGroup.LayoutParams lp = playSurfaceView.getLayoutParams();
                    playSurfaceView.setLayoutParams(lp);
                    display = true;
                }

            }
        });
       
    }
///////////////////////////////////////
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (mediaPlayer == null) {
                flag = false;
            } else if (mediaPlayer.isPlaying()) {
                flag = true;
                int position = mediaPlayer.getCurrentPosition();
                int mMax = mediaPlayer.getDuration();
                int sMax = seekbar.getMax();
                seekbar.setProgress(position * sMax / mMax);
            } else {
                return;
            }
        };
    };

    class upDateSeekBar implements Runnable {

        @Override
        public void run() {
            mHandler.sendMessage(Message.obtain());
            if (flag) {
                mHandler.postDelayed(update, 1000);
            }
        }
    }

    @Override
    protected void onDestroy() {   //activity销毁后，释放资源
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        System.gc();
    }


}

