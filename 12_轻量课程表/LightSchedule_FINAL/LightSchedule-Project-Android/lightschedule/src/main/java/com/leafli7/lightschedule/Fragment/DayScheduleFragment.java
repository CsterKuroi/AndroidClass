package com.leafli7.lightschedule.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.lightschedule.R;
import com.leafli7.lightschedule.Adapter.DayScheduleAdapter;
import com.leafli7.lightschedule.Utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayScheduleFragment extends Fragment {

    String TAG = Constant.TAG + getClass().getSimpleName();

    private int curFragmentDayOfWeek;
    private ListView lvDayScheduleListView;

    public void setCurFragmentDayOfWeek(int curFragmentDayOfWeek){
        this.curFragmentDayOfWeek = curFragmentDayOfWeek;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_schedule, container, false);
//        return inflater.inflate(R.layout.day_schedule_list_item, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
//        LinearLayout mainLl = (LinearLayout) view.findViewById(R.id.mainLl);
//        mainLl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "linear layout clicked!", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "linear layout clicked!");
//            }
//        });


        lvDayScheduleListView = (ListView) view.findViewById(R.id.lvDayScheduleListView);
        lvDayScheduleListView.setAdapter(new DayScheduleAdapter(getActivity(), curFragmentDayOfWeek));

//        Log.e(TAG, TAG + " onStart");
    }

    public ListView getLvDayScheduleListView() {
        return lvDayScheduleListView;
    }
}
