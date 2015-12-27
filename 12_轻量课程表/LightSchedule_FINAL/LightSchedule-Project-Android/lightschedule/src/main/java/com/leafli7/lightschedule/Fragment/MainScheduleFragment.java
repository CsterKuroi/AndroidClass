package com.leafli7.lightschedule.Fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lightschedule.R;
import com.leafli7.lightschedule.Adapter.DayScheduleAdapter;
import com.leafli7.lightschedule.Utils.Constant;

import java.util.HashMap;

import SlidingTabs.SlidingTabLayout;

public class MainScheduleFragment extends android.support.v4.app.Fragment {
    private String TAG = getClass().getSimpleName();
    private View v;

    SlidingTabLayout mSlidingTabLayout;
    ViewPager mViewPager;
    ViewPager.OnPageChangeListener mSlidingTabLayoutOnPageChangeListener;

    android.support.v4.app.FragmentManager fragmentManager;
    MainTabs mainTabs;

    public MainScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_schedule, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        v = getView();
        fragmentManager = getChildFragmentManager();
        if (mainTabs == null) {
            mainTabs = new MainTabs();
        }
        initialFindView();
        initialSlidingTabAndViewPager();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void initialFindView() {
        mViewPager = (ViewPager) v.findViewById(R.id.view_pager_main_schedule_fragment);
        mViewPager.setOffscreenPageLimit(7); // tabcachesize (=tabcount for better performance)

        mSlidingTabLayout = (SlidingTabLayout) v.findViewById(R.id.sliding_tabs_main_schedule_fragment);
    }

    private void initialSlidingTabAndViewPager() {
        // use own style rules for tab layout
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);

        Resources res = getResources();
        mSlidingTabLayout.setSelectedIndicatorColors(res.getColor(R.color.tab_indicator_color));
        mSlidingTabLayout.setDistributeEvenly(true);
        mViewPager.setAdapter(mainTabs);
        mViewPager.setCurrentItem(Constant.CURRENT_DAY_OF_WEEK);   //leafli7 设置初始week选择
        mSlidingTabLayout.setViewPager(mViewPager);

        // Tab events
        mSlidingTabLayoutOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "page : " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(mSlidingTabLayoutOnPageChangeListener);
        }
    }

    class MainTabs extends PagerAdapter {
        String[] week = Constant.DAY_OF_WEEK;
        HashMap<String, DayScheduleFragment> fragmentHashMap = new HashMap<String, DayScheduleFragment>();

        SparseArray<View> views = new SparseArray<View>();

        public MainTabs() {
            int i = 0;
            for (String day : week) {
                DayScheduleFragment curFragment = new DayScheduleFragment();
                curFragment.setCurFragmentDayOfWeek(i);
                fragmentHashMap.put(day, curFragment);
                fragmentManager.beginTransaction().add(curFragment, day).commit();
                i++;
            }
        }


        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return week.length;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return week[position].substring(0, 3);
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View viewCurFragment = fragmentHashMap.get(week[position]).getView();
            container.addView(viewCurFragment);
            views.put(position, viewCurFragment);
            return viewCurFragment;
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            views.remove(position);
        }

        @Override
        public void notifyDataSetChanged() {
            int position = 0;
            for (DayScheduleFragment dayScheduleFragment : fragmentHashMap.values()) {
                ((DayScheduleAdapter) dayScheduleFragment.getLvDayScheduleListView().getAdapter()).notifyDataSetChanged();
            }
            super.notifyDataSetChanged();
        }

    }

}
