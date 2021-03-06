package com.app.youthlive.checkin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.youthlive.R;
import com.app.youthlive.SharePreferenceUtils;
import com.app.youthlive.TotalbroadcastPOJO;
import com.app.youthlive.bean;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.youthlive.bean.getContext;

public class CheckinActivity extends AppCompatActivity {

    static int iMonth;
    public Spinner launchMonthSpinner;
    MyRecyclerViewAdapter adapter;
    Toolbar toolbar;
    TextView total_broadcast, monthheader;
    ProgressBar progressBar;

    TextView badge;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinmain);

        pref = getSharedPreferences("pref" , Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        toolbar.setTitleTextColor(Color.WHITE);
        progressBar = findViewById(R.id.progress);
        monthheader = findViewById(R.id.header);
        total_broadcast = findViewById(R.id.total_broadcast);


        badge = findViewById(R.id.badge);
        //using calender to find number of days in month and current date


        String uType = pref.getString("userType" , "");

        if (uType.equals("user"))
        {
            badge.setVisibility(View.GONE);
        }
        else
        {
            badge.setVisibility(View.VISIBLE);
            badge.setText("EMPLOYEE ID : " + pref.getString("yid" , ""));
        }

        Calendar c1 = Calendar.getInstance();
        iMonth = c1.get(Calendar.MONTH); // 1 (months begin with 0)

        //INITIALIZE VIEWS
        launchMonthSpinner = findViewById(R.id.spinner);


        launchMonthSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        fillMonths();

        launchMonthSpinner.setSelection(iMonth);
        launchMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextSize(14);

                sendData();
                monthsettext(position, iMonth);
                totalbroadcast(String.valueOf(position + 1));
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        totalbroadcast(String.valueOf(iMonth + 1));
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
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout2);

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


    public void totalbroadcast(String month) {
        total_broadcast.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final bean b = (bean) getContext().getApplicationContext();

        Call<TotalbroadcastPOJO> call = b.getRetrofit().totalbroadcast(SharePreferenceUtils.getInstance().getString("userId"), month);

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<TotalbroadcastPOJO>() {
            @Override
            public void onResponse(Call<TotalbroadcastPOJO> call, Response<TotalbroadcastPOJO> response) {

                progressBar.setVisibility(View.GONE);
                total_broadcast.setVisibility(View.VISIBLE);
                try {
                    if (!response.body().getTotalMonthlyBroadcast().toString().isEmpty()) {
                        long totalSecs = response.body().getTotalMonthlyBroadcast();
                        long hours = totalSecs / 3600;
                        long minutes = (totalSecs % 3600) / 60;
                        long seconds = totalSecs % 60;

                        String timeString = String.format("%02d hr %02d min %02d sec", hours, minutes, seconds);
                        total_broadcast.setText(timeString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<TotalbroadcastPOJO> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                total_broadcast.setVisibility(View.VISIBLE);
            }

        });
    }

    public void monthsettext(int month, int currentmonth) {
        if (month == currentmonth) {
            monthheader.setText("This Month till now");
        } else {

            switch (month) {
                case 0:
                    monthheader.setText("Your January Month Checkin");
                    break;
                case 1:
                    monthheader.setText("Your February Month Checkin");
                    break;
                case 2:
                    monthheader.setText("Your March Month Checkin");
                    break;
                case 3:
                    monthheader.setText("Your April Month Checkin");
                    break;
                case 4:
                    monthheader.setText("Your May Month Checkin");
                    break;
                case 5:
                    monthheader.setText("Your June Month Checkin");
                    break;
                case 6:
                    monthheader.setText("Your July Month Checkin");
                    break;
                case 7:
                    monthheader.setText("Your August Month Checkin");
                    break;
                case 8:

                    monthheader.setText("Your September Month Checkin");
                    break;
                case 9:
                    monthheader.setText("Your October Month Checkin");
                    break;
                case 10:
                    monthheader.setText("Your November Month Checkin");
                    break;
                case 11:
                    monthheader.setText("Your December Month Checkin");
                    break;
            }
        }
    }
}
