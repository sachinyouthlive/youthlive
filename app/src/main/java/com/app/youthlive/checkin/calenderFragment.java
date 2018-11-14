package com.app.youthlive.checkin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.youthlive.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class calenderFragment extends Fragment implements View.OnClickListener, MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    private IFragmentToActivity mCallback;
    private Button btnFtoA;
    private Button btnFtoF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calender_fragment, container, false);
        String monthActivity = this.getArguments().getString("imonth");


        /////////////////
        //using calender to find number of days in month and current date


        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String formattedDate = df.format(c1.getTime());
        // formattedDate have current date/time
        int month = this.getArguments().getInt("YEAR_KEY");

        Log.e("month", String.valueOf(month));
        int iYear = c1.get(Calendar.YEAR);
        int iMonth = c1.get(Calendar.MONTH);
        int iDay = c1.get(Calendar.DATE);

        c1.set(iYear, month, iDay);
        int maxDay = c1.getActualMaximum(Calendar.DAY_OF_MONTH);


        // int daysInMonth = mycal.getActualMaximum(monthFinal); // 28
        int daysInMonth = maxDay;


        // data to populate the RecyclerView with

        int[] daa = new int[daysInMonth];
        int i = 1;
        while (i <= daysInMonth) {

            daa[i - 1] = i;
            i++;

        }

        // set up the RecyclerView
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvNumberfrag);
        int numberOfColumns = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(getActivity(), daa, month);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                break;

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);

    }
}
