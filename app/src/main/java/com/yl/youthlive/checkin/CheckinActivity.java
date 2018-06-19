package com.yl.youthlive.checkin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yl.youthlive.R;

import java.util.Calendar;

public class CheckinActivity extends AppCompatActivity {

    static int iMonth;
    public Spinner launchMonthSpinner;
    MyRecyclerViewAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinmain);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitleTextColor(Color.WHITE);

        //using calender to find number of days in month and current date

        Calendar c1 = Calendar.getInstance();
        iMonth = c1.get(Calendar.MONTH); // 1 (months begin with 0)

        //INITIALIZE VIEWS
        launchMonthSpinner = findViewById(R.id.spinner);

        fillMonths();

        launchMonthSpinner.setSelection(iMonth);
        launchMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextSize(14);

                sendData();
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void sendData() {
        //PACK DATA IN A BUNDLE
        Bundle bundle = new Bundle();
        bundle.putInt("YEAR_KEY", Integer.valueOf(launchMonthSpinner.getSelectedItemPosition()));

        //PASS OVER THE BUNDLE TO OUR FRAGMENT
        calenderFragment myFragment = new calenderFragment();
        myFragment.setArguments(bundle);

        launchMonthSpinner.setSelection(Integer.valueOf(launchMonthSpinner.getSelectedItemPosition()));

        //THEN NOW SHOW OUR FRAGMENT
        getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).commit();
    }

    /*
    FILL YEARS IN OUR SPINNER
     */
    private void fillMonths() {
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        adapter.add("JANUARY");
        adapter.add("FEBRUARY");
        adapter.add("MARCH");
        adapter.add("APRIL");
        adapter.add("MAY");
        adapter.add("JUNE");
        adapter.add("JULY");
        adapter.add("AUGUST");
        adapter.add("SEPTEMBER");
        adapter.add("OCTOBER");
        adapter.add("NOVEMBER");
        adapter.add("DECEMBER");

        //SET ADAPTER INSTANCE TO OUR SPINNER
        launchMonthSpinner.setAdapter(adapter);
    }

}
