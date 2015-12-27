package com.leafli7.lightschedule.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.lightschedule.R;
import com.leafli7.lightschedule.Entity.Lesson;
import com.leafli7.lightschedule.Utils.Constant;
import com.leafli7.lightschedule.Utils.OwnDbHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

public class AddLessonActivity extends AppCompatActivity {
    private String TAG = Constant.TAG + getClass().getSimpleName();
    public static final String MODE = "MODE";
    public static final int VIEW_MODE = 1;
    public static final int MODIFY_MODE = 2;
    public static final int ADD_MODE = 3;

    private Toolbar mToolbar;
    private EditText mEtLessonTitleEditText;
    private EditText mEtClassroomEditText;
    private EditText mEtTeacherNameEditText;
    private Spinner mSpLessonTimeNumSpinner;
    private Spinner mSpDayOfWeekSpinner;
    private Spinner mSpStartWeekSpinner;
    private Spinner mSpEndWeekSpinner;
    private CheckBox mCbIsTinyLessonCheckBox;
    private RadioButton mRbFirstRadioButton;
    private RadioButton mRbSecondRadioButton;
    private RadioGroup mRgIsTinyLessonRadioGroup;
    private CheckBox mCbIsSingleWeekLessonCheckBox;
    private RadioButton mRbOddRadioButton;
    private RadioButton mRbEvenRadioButton;
    private RadioGroup mRgIsSingleWeekLessonRadioGroup;
    private Menu menu;

    private ArrayAdapter<String> weekAdapater;
    private ArrayAdapter<String> dayAdapter;
    private ArrayAdapter<String> sectionAdapter;

    private int curMode = 0;
    private Lesson curLesson;
    private int startWeek = 0, endWeek = 0, lessonTimeNum = 0, dayOfWeek = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson);
        initialStatue();
        initialFindView();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        curLesson = null;
        curMode = (int) bundle.get(MODE);
        switch (curMode) {
            case VIEW_MODE:
                curLesson = bundle.getParcelable("lesson");
                initialViewModeWidgetText();
                setEditMode(false);
                mToolbar.setTitle(R.string.view_lesson);
                break;
            case ADD_MODE:
                curLesson = new Lesson();
                initialAddModeWidgetText();
                mToolbar.setTitle(R.string.title_activity_add_lesson);
            case MODIFY_MODE:
                mToolbar.setTitle(R.string.modify_lesson);
                setEditMode(true);
                break;
            default:
                Log.e(TAG, "Wrong mode!");
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initialStatue() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(ContextCompat.getColor(this, R.color.colorMainDark));
        }
    }

    private void initialFindView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        mEtLessonTitleEditText = (EditText) findViewById(R.id.etLessonTitle);
        mEtClassroomEditText = (EditText) findViewById(R.id.etClassroom);
        mEtTeacherNameEditText = (EditText) findViewById(R.id.etTeacherName);
        mSpLessonTimeNumSpinner = (Spinner) findViewById(R.id.spLessonTimeNum);
        mSpDayOfWeekSpinner = (Spinner) findViewById(R.id.spDayOfWeek);
        mSpStartWeekSpinner = (Spinner) findViewById(R.id.spStartWeek);
        mSpEndWeekSpinner = (Spinner) findViewById(R.id.spEndWeek);
        mCbIsTinyLessonCheckBox = (CheckBox) findViewById(R.id.cbIsTinyLesson);
        mRbFirstRadioButton = (RadioButton) findViewById(R.id.rbFirst);
        mRbSecondRadioButton = (RadioButton) findViewById(R.id.rbSecond);
        mRgIsTinyLessonRadioGroup = (RadioGroup) findViewById(R.id.rgIsTinyLesson);
        mCbIsSingleWeekLessonCheckBox = (CheckBox) findViewById(R.id.cbIsSingleWeekLesson);
        mRbOddRadioButton = (RadioButton) findViewById(R.id.rbOdd);
        mRbEvenRadioButton = (RadioButton) findViewById(R.id.rbEven);
        mRgIsSingleWeekLessonRadioGroup = (RadioGroup) findViewById(R.id.rgIsSingleWeekLesson);

        mCbIsTinyLessonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRgIsTinyLessonRadioGroup.setVisibility(View.VISIBLE);
                    mRbFirstRadioButton.setChecked(true);
                } else {
                    mRgIsTinyLessonRadioGroup.setVisibility(View.INVISIBLE);
                }
            }
        });
        mCbIsSingleWeekLessonCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mRgIsSingleWeekLessonRadioGroup.setVisibility(View.VISIBLE);
                    mRbOddRadioButton.setChecked(true);
                } else {
                    mRgIsSingleWeekLessonRadioGroup.setVisibility(View.INVISIBLE);
                }
            }
        });

        String[] weekArray = new String[Constant.WEEK_NUMS];
        for (int i = 0; i < weekArray.length; i++) {
            weekArray[i] = String.valueOf(i + 1);
        }
        weekAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weekArray);
        weekAdapater.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpStartWeekSpinner.setAdapter(weekAdapater);
        mSpEndWeekSpinner.setAdapter(weekAdapater);
        mSpStartWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startWeek = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSpEndWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                endWeek = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        dayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.DAY_OF_WEEK);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpDayOfWeekSpinner.setAdapter(dayAdapter);
        mSpDayOfWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayOfWeek = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        String[] sectionArray = new String[Constant.LESSEN_TIME_ACCOUNT];
        for (int i = 0; i < sectionArray.length; i++) {
            sectionArray[i] = String.valueOf(i + 1);
        }
        sectionAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sectionArray);
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpLessonTimeNumSpinner.setAdapter(sectionAdapter);
        mSpLessonTimeNumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lessonTimeNum = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void initialViewModeWidgetText() {
        mEtLessonTitleEditText.setText(curLesson.getName());
        mEtClassroomEditText.setText(curLesson.getClassroom());
        mEtTeacherNameEditText.setText(curLesson.getTeacherName());
        if (curLesson.isTinyLesson()) {
            mCbIsTinyLessonCheckBox.setChecked(true);
            if (curLesson.isFirstHalf()) {
                mRbFirstRadioButton.setChecked(true);
            } else {
                mRbSecondRadioButton.setChecked(true);
            }
        }

        if (curLesson.isSingleWeekLesson()) {
            mCbIsSingleWeekLessonCheckBox.setChecked(true);
            if (curLesson.isOddWeekLesson()) {
                mRbOddRadioButton.setChecked(true);
            } else {
                mRbEvenRadioButton.setChecked(true);
            }
        }

        mSpStartWeekSpinner.setSelection(curLesson.getStartWeek());
        mSpEndWeekSpinner.setSelection(curLesson.getEndWeek());
        mSpDayOfWeekSpinner.setSelection(curLesson.getDayOfWeek());
        mSpLessonTimeNumSpinner.setSelection(curLesson.getLessonTimeNum());
    }

    private void initialAddModeWidgetText() {
        Intent i = getIntent();
        Bundle b = i.getExtras();
        mSpDayOfWeekSpinner.setSelection(b.getInt("dayOfWeek"));
        mSpLessonTimeNumSpinner.setSelection(b.getInt("lessonTimeNum"));
    }

    public void setEditMode(boolean isEditable) {
        mEtLessonTitleEditText.setEnabled(isEditable);
        mEtClassroomEditText.setEnabled(isEditable);
        mEtTeacherNameEditText.setEnabled(isEditable);
        mSpLessonTimeNumSpinner.setEnabled(isEditable);
        mSpDayOfWeekSpinner.setEnabled(isEditable);
        mSpStartWeekSpinner.setEnabled(isEditable);
        mSpEndWeekSpinner.setEnabled(isEditable);
        mCbIsTinyLessonCheckBox.setEnabled(isEditable);
        mRbFirstRadioButton.setEnabled(isEditable);
        mRbSecondRadioButton.setEnabled(isEditable);
        mRgIsTinyLessonRadioGroup.setEnabled(isEditable);
        mCbIsSingleWeekLessonCheckBox.setEnabled(isEditable);
        mRbOddRadioButton.setEnabled(isEditable);
        mRbEvenRadioButton.setEnabled(isEditable);
        mRgIsSingleWeekLessonRadioGroup.setEnabled(isEditable);

        if (!isEditable) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }

    private void updateCurLesson() {
        curLesson.setClassroom(mEtClassroomEditText.getText().toString());
        curLesson.setName(mEtLessonTitleEditText.getText().toString());
        curLesson.setTeacherName(mEtTeacherNameEditText.getText().toString());
        curLesson.setIsTinyLesson(mCbIsTinyLessonCheckBox.isChecked());
        if (mCbIsTinyLessonCheckBox.isChecked()) {
            if (mRbFirstRadioButton.isChecked()) {
                curLesson.setIsFirstHalf(true);
            } else {
                curLesson.setIsFirstHalf(false);
            }
        }
        curLesson.setIsSingleWeekLesson(mCbIsSingleWeekLessonCheckBox.isChecked());
        if (mCbIsSingleWeekLessonCheckBox.isChecked()) {
            if (mRbOddRadioButton.isChecked()) {
                curLesson.setIsOddWeekLesson(true);
            } else {
                curLesson.setIsOddWeekLesson(false);
            }
        }

        curLesson.setStartWeek(startWeek);
        curLesson.setEndWeek(endWeek);
        curLesson.setDayOfWeek(dayOfWeek);
        curLesson.setLessonTimeNum(lessonTimeNum);
    }

    private void addLesson() {
        updateCurLesson();
        OwnDbHelper dbHelper = new OwnDbHelper(this);
        int insertId = dbHelper.insertLesson(curLesson);
        curLesson.setId(insertId);
        Constant.weekSchedule.get(curLesson.getDayOfWeek()).get(curLesson.getLessonTimeNum()).add(curLesson);
//        Constant.initialWeekSchedule();
    }

    //TODO : 将刷新weekschedule修改为查找新插入的lesson在数据库中的id然后赋值
    private void modifyLesson() {
        OwnDbHelper dbHelper = new OwnDbHelper(this);
        //查找删除
        ArrayList<Lesson> lessons = Constant.weekSchedule.get(curLesson.getDayOfWeek()).get(curLesson.getLessonTimeNum());
        for (int i = 0; i < lessons.size(); i++) {
            if (lessons.get(i).getId() == curLesson.getId()) {
                lessons.remove(i);
                break;
            }
        }
        updateCurLesson();
        dbHelper.updateLesson(curLesson);
        Constant.weekSchedule.get(curLesson.getDayOfWeek()).get(curLesson.getLessonTimeNum()).add(curLesson);
//        Constant.initialWeekSchedule();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        switch (curMode) {
            case ADD_MODE:
                getMenuInflater().inflate(R.menu.menu_add_lesson, menu);
                break;
            case VIEW_MODE:
                getMenuInflater().inflate(R.menu.menu_add_lesson_view_mode, menu);
                break;
            case MODIFY_MODE:
                getMenuInflater().inflate(R.menu.menu_add_lesson_modify, menu);
                break;
            default:
                Log.e(TAG, "wrong mode in menu created!");
        }
        this.menu = menu;
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
        } else if (id == android.R.id.home) {
            Log.e(TAG, "Home clicked");
            onBackPressed();
        } else if (id == R.id.action_modify) {
            curMode = MODIFY_MODE;
            setEditMode(true);
            menu.clear();
            mToolbar.setTitle(R.string.modify_lesson);
            getMenuInflater().inflate(R.menu.menu_add_lesson_modify, menu);

        } else if (id == R.id.action_finish_modify) {
            modifyLesson();
            finish();
//            getMenuInflater().inflate(R.menu.menu_add_lesson_view_mode, menu);
        } else if (id == R.id.action_add) {
            addLesson();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (curMode == ADD_MODE || curMode == MODIFY_MODE) {
            new AlertDialog.Builder(this).setTitle("Exit Editing?").setMessage("Confirm to give up?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {
            finish();
        }
    }
}
