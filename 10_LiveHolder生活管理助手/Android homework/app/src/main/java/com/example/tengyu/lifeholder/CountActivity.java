package com.example.tengyu.lifeholder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.example.tengyu.lifeholder.tomato.tomatoNotes;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.sefford.circularprogressdrawable.CircularProgressDrawable;

import java.util.Date;
import java.util.Random;

import cn.iwgang.countdownview.CountdownView;

public class CountActivity extends AppCompatActivity {
    ImageView tomato_progress;
    CircularProgressButton tomato_fight;
    private CircularProgressDrawable drawable;
    private CountdownView tomato_count;
    private NiftyDialogBuilder cancelWarning;
    private Random seed = new Random();
    private String Title;
    private long stime;
    private long GoalTime = 0;
    private boolean ifRemind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tomatotask_count_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView tomato_title = (TextView) findViewById(R.id.tomatotask_count_title);
        Title = getIntent().getStringExtra("TITLE");
        tomato_title.setText(Title);
        if(getIntent().getStringExtra("REMIND").equals("TRUE"))
            ifRemind = true;
        else  ifRemind = false;

        tomato_title.setSelected(true);
        tomato_progress = (ImageView) findViewById(R.id.tomato_drawable);

        drawable = new CircularProgressDrawable.Builder()
                .setRingWidth(getResources().getDimensionPixelSize(R.dimen.drawable_ring_size))
                .setOutlineColor(getResources().getColor(android.R.color.darker_gray))
                .setRingColor(getResources().getColor(R.color.themeHolyLight))
                .setCenterColor(getResources().getColor(R.color.themeLight))
                .create();
        drawable.setCircleScale(0);

        tomato_progress.setImageDrawable(drawable);

        tomato_fight = (CircularProgressButton) findViewById(R.id.tomatotask_count_fightbtn);
        tomato_fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tomato_fight.getProgress() == 0) {
                    tomato_fight.setProgress(-1);
                    stime = 25 * 60 * 1000;
                    GoalTime = (new Date()).getTime()+stime;
                    prepareStyle1Animation().start();
                } else if (tomato_fight.getProgress() == 100) {
                    Back2Main(true);
                } else
                    tomato_fight.setProgress(-1);
            }
        });

        tomato_count =(CountdownView) findViewById(R.id.tomatotask_counts);

        cancelWarning = NiftyDialogBuilder.getInstance(this);
        cancelWarning.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelWarning.dismiss();//DO NOTHING
                if(tomato_fight.getProgress()==100){
                    Back2Main(true);
                }
                else{
                    Back2Main(false);
                }
            }
        });
        cancelWarning.setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelWarning.dismiss();//DO NOTHING
            }
        });
        cancelWarning.setCancelable(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), tomatoNotes.sntArray[seed.nextInt(tomatoNotes.LENGTH)],Toast.LENGTH_LONG).show();
            }
        });

        if(savedInstanceState != null){
            //stime = savedInstanceState.getLong("TIME_REMAIN");
            stime = savedInstanceState.getLong("TIME_GOAL") - (new Date()).getTime();
            if(stime<=0)
                tomato_fight.setProgress(100);
            else
                prepareStyle1Animation().start();
        }
    }

    private Animator prepareStyle1Animation() {
        AnimatorSet animation = new AnimatorSet();
        final Animator indeterminateAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.PROGRESS_PROPERTY, 0, 3600);
        indeterminateAnimation.setDuration(3600);

        Animator innerCircleAnimation = ObjectAnimator.ofFloat(drawable, CircularProgressDrawable.CIRCLE_SCALE_PROPERTY, 0f, 0.75f);
        innerCircleAnimation.setDuration(3600);
        innerCircleAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                drawable.setIndeterminate(true);
                tomato_count.start(stime);
                //mHandler3 .postDelayed(mRunnable3, stime);
                (new tomatoCount(stime,stime-1)).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                indeterminateAnimation.end();
                drawable.setIndeterminate(false);
                drawable.setProgress(0);
            }
        });

        animation.playTogether(innerCircleAnimation, indeterminateAnimation);
        return animation;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //long tempDate = tomato_count.getRemainTime();
        //outState.putLong("TIME_REMAIN",tempDate);
        if(tomato_fight.getProgress()!=0){
            outState.putLong("TIME_GOAL", GoalTime);
        }
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(tomato_fight.getProgress()==100){
            Back2Main(true);
        }
        else if(tomato_fight.getProgress()==-1){
            cancelWarning.withTitle(getResources().getString(R.string.message_title_warning))
                    .withMessage(" "+getResources().getString(R.string.message_content_leave))             //.withMessage(null)  no Msg
                    .withDialogColor(getResources().getColor(R.color.themeLight))
                    .withDialogColor(getResources().getColor(R.color.theme))
                    .withIcon(getResources().getDrawable(R.drawable.ic_action_warning))
                    .withDuration(600)                                          //def
                    .withEffect(Effectstype.RotateBottom)                               //def Effectstype.Slidetop
                    .withButton1Text(getResources().getString(R.string.message_yes))                                      //def gone
                    .withButton2Text(getResources().getString(R.string.message_cancel))                                  //def gone
                    .isCancelableOnTouchOutside(true).show();
        }
        else{
            Back2Main(false);
        }
    }

    public void Back2Main(boolean ifOK){
        //Intent intent = new Intent();
        Intent intent = new Intent(CountActivity.this,MainActivity.class);
        if(ifOK) {
            intent.putExtra("RESULT",Title);
            startActivity(intent);
            finish();
            //setResult(RESULT_FIRST_USER, intent);
            //finish();
        }
        else{
            intent.putExtra("RESULT","");
            startActivity(intent);
            finish();
            //setResult(RESULT_CANCELED,intent);
            //finish();
        }
    }

    class tomatoCount extends CountDownTimer {
        public tomatoCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            tomato_fight.setProgress(0);
            if(ifRemind){
                Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                long[] pattern = {1000, 2000, 1000, 2000, 1000, 2000};
                vibrator.vibrate(pattern, -1);
            }
            simulateSuccessProgress(tomato_fight);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

}
