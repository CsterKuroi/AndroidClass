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
    private Button buttonstop;  //���ڿ�ʼ����ͣ�İ�ť
    private Button buttonplay;
    private SurfaceView playSurfaceView;   //��ͼ�����������ڰ���Ƶ��ʾ����Ļ��
    private String url = null;   //��Ƶ���ŵ�ַ
    private MediaPlayer mediaPlayer;    //�������ؼ�
    private int postSize;    //�����岥��Ƶ��С
    private SeekBar seekbar;   //�������ؼ�
    private boolean flag = true;   //�����ж���Ƶ�Ƿ��ڲ�����
    private RelativeLayout relativeLayout;
    private boolean display;   //�����Ƿ���ʾ������ť

    private View progressBar;   //ProgressBar
    private upDateSeekBar update;   //���½�������
    /////////////////////////////////////////
   
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);   //ȫ��
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // Ӧ������ʱ��������Ļ������������
        init();  //��ʼ������
    }
    private void init() {
        mediaPlayer = new MediaPlayer();   //����һ������������
        update = new upDateSeekBar();  //�������½���������
        
        setContentView(R.layout.activity_vedio_play);   //���ز����ļ�
        
        seekbar = (SeekBar) findViewById(R.id.seekbar);  //������
        
        buttonplay = (Button) findViewById(R.id.play);
        buttonplay.setEnabled(false); //�ս����������䲻�ɵ��
        
        buttonstop = (Button) findViewById(R.id.stop);
        buttonstop.setEnabled(false); //�ս����������䲻�ɵ��
        
        playSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);
  //      playSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);   //������
        playSurfaceView.getHolder().setKeepScreenOn(true);   //������Ļ����
        playSurfaceView.getHolder().addCallback(new surFaceView());   //���ü����¼�
     
        relativeLayout = (RelativeLayout) findViewById(R.id.rl2);
        progressBar = findViewById(R.id.pb);
        setListener();
    }

    class PlayMovie extends Thread {   //������Ƶ���߳�

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
               
                mediaPlayer.setDataSource(url);   //���ò���·��
                
                mediaPlayer.setDisplay(playSurfaceView.getHolder());  //����Ƶ��ʾ��SurfaceView��
                mediaPlayer.setOnPreparedListener(new PreparedListener(post) );  //���ü����¼�
                mediaPlayer.prepare();  //׼������
       
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
                mediaPlayer.start();  //��ʼ������Ƶ
            } else {
                return;
            }      */
            if (postSize > 0) {  //˵����;ֹͣ����activity���ù�pase�����������û����ֹͣ��ť��������ֹͣʱ��λ�ÿ�ʼ����
                mediaPlayer.seekTo(postSize);   //����postSize��Сλ�ô����в���
            }
            new Thread(update).start();   //�����̣߳����½�����
        }
    }

    
    private class surFaceView implements Callback {     //����󶨵ļ������¼�

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {   //������ɺ����
            if (postSize > 0 && url!= null) {    //˵����ֹͣ��activity���ù�pase����������ֹͣλ�ò���
                new PlayMovie(postSize).start();
                flag = true;
                int sMax = seekbar.getMax();
                int mMax = mediaPlayer.getDuration();
                seekbar.setProgress(postSize * sMax / mMax);
                postSize = 0;
                progressBar.setVisibility(View.GONE);
            }
            else {
                new PlayMovie(0).start();   //�����ǵ�һ�ο�ʼ����
            }
        }
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) { //activity���ù�pase���������浱ǰ����λ��
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
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() { //��Ƶ�������
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
 * �����Ƶ�ڲ��ţ������mediaPlayer.pause();��ֹͣ������Ƶ����֮��mediaPlayer.start()  ��ͬʱ����ť����
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
                   	progressBar.setVisibility(View.GONE);  //׼����ɺ����ؿؼ�      	
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
         * ���½�����
         */
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int value = seekbar.getProgress() * mediaPlayer.getDuration()  //�����������Ҫǰ����λ�����ݴ�С
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
 * �����Ļ���л��ؼ�����ʾ����ʾ��Ӧ�أ����أ�����ʾ
 */
        playSurfaceView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (display) {
                    buttonstop.setVisibility(View.GONE);
                    buttonplay.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE); 
                    progressBar.setVisibility(View.GONE);  //׼����ɺ����ؿؼ�      	
                   	seekbar.setVisibility(View.GONE); 
                    display = false;
                } else {
                	relativeLayout.setVisibility(View.VISIBLE);
                    buttonstop.setVisibility(View.VISIBLE);
                    buttonplay.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);  //׼����ɺ����ؿؼ�      	
                   	seekbar.setVisibility(View.VISIBLE); 
                    playSurfaceView.setVisibility(View.VISIBLE);
                    /**
                     * ���ò���Ϊȫ��
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
    protected void onDestroy() {   //activity���ٺ��ͷ���Դ
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        System.gc();
    }


}

