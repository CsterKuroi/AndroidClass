package com.example.tengyu.lifeholder;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import com.dd.CircularProgressButton;
import com.example.tengyu.lifeholder.tomato.tomatoTask;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity implements com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener, com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener {
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    private Boolean ifEdit = false;
    private RangeBar tomato_rangebar;
    private TextView tomato_index;
    private TextView tomato_deadline;
    private TextView tomato_deadline2;
    private EditText tomato_title;
    private NiftyDialogBuilder cancelWarning;
    private com.kyleduo.switchbutton.SwitchButton tomato_ifRemind;
    private tomatoTask tomato;
    private com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog;
    private com.sleepbot.datetimepicker.time.TimePickerDialog timePickerDialog;
    private CircularProgressButton tomato_save;

    private int year;
    private int month;
    private int day;
    private int hours;
    private int minutes;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent();
            if(tomato.getTitle().equals("")==false){
                Bundle bundle = new Bundle();
                bundle.putParcelable(MainActivity.PAR_KEY, tomato);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
            }
            else
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tomatotask_edit_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //View Init
        tomato_index = (TextView) findViewById(R.id.tomatotask_edit_tomatoIndex);
        tomato_deadline = (TextView) findViewById(R.id.tomatotask_edit_deadline);
        tomato_deadline2 = (TextView) findViewById(R.id.tomatotask_edit_deadline2);
        tomato_title = (EditText) findViewById(R.id.tomatotask_edit_title);

        //Get tomato send by Main Activity
        tomato = getIntent().getParcelableExtra(MainActivity.PAR_KEY);

        tomato_rangebar = (RangeBar) findViewById(R.id.tomatotask_edit_rangebar);

        cancelWarning = NiftyDialogBuilder.getInstance(this);

        cancelWarning.setButton1Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelWarning.dismiss();//DO NOTHING
                tomato_save.callOnClick();
            }
        });
        cancelWarning.setButton2Click(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelWarning.dismiss();//DO NOTHING
                Intent intent = new Intent();
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
        cancelWarning.setCancelable(true);

        //Init edit deadline button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tomatotask_edit_deadlinebtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        //Save DataPickerDiag
        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

        //Init tomato_if Remind me
        tomato_ifRemind = (com.kyleduo.switchbutton.SwitchButton) findViewById(R.id.tomatotask_edit_ifRemind);
        tomato_ifRemind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tomato.setIfRemind(isChecked);
                ifEdit = true;
            }
        });

        //Edit tomato.priority level
        RadioGroup tomato_lev = (RadioGroup) findViewById(R.id.tomatotask_edit_levGroup);
        tomato_lev.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tomatotask_edit_lev3) {
                    tomato.setLev(3);
                } else if (checkedId == R.id.tomatotask_edit_lev2) {
                    tomato.setLev(2);
                } else {
                    tomato.setLev(1);
                }
                ifEdit = true;
            }
        });

        //Init save behavior
        tomato_save = (CircularProgressButton) findViewById(R.id.tomatotask_edit_savebtn);
        tomato_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tomato_save.getProgress() == 0) {
                    if(tomato.getTitle().equals("")==false) {
                        simulateSuccessProgress(tomato_save);
                        Calendar cr = Calendar.getInstance();
                        cr.set(year, month, day, hours, minutes);
                        tomato.setDeadline(cr.getTime());
                        mHandler .postDelayed(mRunnable, 2000); // 在Handler中执行子线程并延迟3s。
                    } else{
                        simulateErrorProgress(tomato_save);
                    }
                } else
                    tomato_save.setProgress(0);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        if(!tomato.getTitle().equals("")) {
            tomato_title.setText(tomato.getTitle());
            tomato_title.setFocusable(false);
        }
        else{
            tomato_title.addTextChangedListener(new TextWatcher(){
                public void afterTextChanged(Editable s) {
                    tomato.setTitle(tomato_title.getText().toString());
                    ifEdit = true;
                }
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    tomato.setTitle(tomato_title.getText().toString());
                    ifEdit = true;
                }
                public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                    //TODO
                }
            });
        }
        if(!tomato.repOK()||tomato.getTomato()==0)
            tomato_rangebar.setEnabled(false);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(tomato.getDeadline());

        RadioButton tomato_lev_radio;
        switch(tomato.getLev()){
            case 3:
                tomato_lev_radio = (RadioButton)findViewById(R.id.tomatotask_edit_lev3);
                tomato_lev_radio.setChecked(true);
                break;
            case 2:
                tomato_lev_radio = (RadioButton)findViewById(R.id.tomatotask_edit_lev2);
                tomato_lev_radio.setChecked(true);
                break;
            default:
                tomato_lev_radio = (RadioButton)findViewById(R.id.tomatotask_edit_lev1);
                tomato_lev_radio.setChecked(true);
                break;
        }
        if(tomato.getTomato()>0){
            tomato_rangebar.setRangePinsByIndices(0, tomato.getTomato() - 1);
            tomato_rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    tomato_index.setText(String.valueOf(rightPinIndex + 1));
                    tomato.setTomato(rightPinIndex + 1);
                    ifEdit = true;
                }
            });
        }
        else
            tomato_rangebar.setRangePinsByIndices(0, 0);

        tomato_index.setText(String.valueOf(tomato.getTomato()));
        tomato_ifRemind.setChecked(tomato.IfRemind());

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hours = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        String tpstr = "";
        if(minutes<10)
            tpstr = "0";
        tomato_deadline.setText(String.valueOf(year) + "-" + String.valueOf(month+1) + "-" + String.valueOf(day));
        tomato_deadline2.setText(String.valueOf(hours) + ":" + tpstr + String.valueOf(minutes));
        datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(this, year, month, day, false);
        timePickerDialog = com.sleepbot.datetimepicker.time.TimePickerDialog.newInstance(this, hours, minutes, false, false);
        datePickerDialog.setYearRange(tomatoTask.MIN_YEAR, tomatoTask.MAX_YEAR);

        ifEdit = false;
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
        if(ifEdit) {
            cancelWarning.withTitle(getResources().getString(R.string.message_title_upload))
                    .withMessage(" "+getResources().getString(R.string.message_content_upload))             //.withMessage(null)  no Msg
                    .withDialogColor(getResources().getColor(R.color.themeLight))
                    .withDialogColor(getResources().getColor(R.color.theme))
                    .withIcon(getResources().getDrawable(R.drawable.ic_action_info))
                    .withDuration(600)                                          //def
                    .withEffect(Effectstype.RotateBottom)                               //def Effectstype.Slidetop
                    .withButton1Text(getResources().getString(R.string.message_yes))                                      //def gone
                    .withButton2Text(getResources().getString(R.string.message_no))                                  //def gone
                    .isCancelableOnTouchOutside(true).show();
        }
        else{
            Intent intent = new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
        }
    }

    //SaveButton Animation
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

    private void simulateErrorProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 99);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
                if (value == 99) {
                    button.setProgress(-1);
                    Toast.makeText(getApplicationContext(),"Check your title",Toast.LENGTH_SHORT).show();
                }
            }
        });
        widthAnimation.start();
    }

    //AboutDataSet
    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int day) {
        tomato_deadline.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
        this.year = year;
        this.month = month;
        this.day = day;
        timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
        ifEdit = true;
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        this.hours = hourOfDay;
        this.minutes = minute;
        String tpstr = "";
        if(minute<10)
            tpstr = "0";
        tomato_deadline2.setText(String.valueOf(hourOfDay) + ":" + tpstr + String.valueOf(minute));
        ifEdit = true;
    }

}
