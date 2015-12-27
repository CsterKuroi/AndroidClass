package com.heyu.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.herveweather.R;
import com.heyu.adapter.ViewPagerAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("InflateParams") public class GuideActivity extends Activity implements OnPageChangeListener {

    private ViewPager vp; // ʹ�ð�׿��ViewPager��ҳ�滬���ؼ�
    private ViewPagerAdapter vpAdapter; // ������
    private List<View> views;

    // �ײ�С��ͼƬ
    private ImageView[] dots;

    // ��¼��ǰѡ��λ��
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);

        // ��ʼ��ҳ��
        initViews();

        // ��ʼ���ײ�С��
        initDots();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this); // ����һ�У�������������������������������������

        views = new ArrayList<View>();
        // ��ʼ������ͼƬ�б�
        views.add(inflater.inflate(R.layout.what_new_one, null));
        views.add(inflater.inflate(R.layout.what_new_two, null));
        views.add(inflater.inflate(R.layout.what_new_three, null));
        views.add(inflater.inflate(R.layout.what_new_four, null));

        // ��ʼ��Adapter
        vpAdapter = new ViewPagerAdapter(views, this);

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // �󶨻ص�
        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // ѭ��ȡ��С��ͼƬ
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// ����Ϊ��ɫ
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1 || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    // ������״̬�ı�ʱ����
    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // ����ǰҳ�汻����ʱ����
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    // ���µ�ҳ�汻ѡ��ʱ����
    @Override
    public void onPageSelected(int arg0) {
        // ���õײ�С��ѡ��״̬
        setCurrentDot(arg0);
    }

}

