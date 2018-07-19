package com.yl.youthlive.checkin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.TotalbroadcastPOJO;
import com.yl.youthlive.bean;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.yl.youthlive.bean.getContext;

public class CheckinActivity extends AppCompatActivity {

    static int iMonth;
    public Spinner launchMonthSpinner;
    MyRecyclerViewAdapter adapter;
    Toolbar toolbar;
    TextView total_broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinmain);
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

        total_broadcast = findViewById(R.id.total_broadcast);
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void totalbroadcast(String month) {

        final bean b = (bean) getContext().getApplicationContext();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllAPIs cr = retrofit.create(AllAPIs.class);
        Call<TotalbroadcastPOJO> call = cr.totalbroadcast(b.userId, month);

        Log.d("userId", b.userId);

        call.enqueue(new Callback<TotalbroadcastPOJO>() {
            @Override
            public void onResponse(Call<TotalbroadcastPOJO> call, Response<TotalbroadcastPOJO> response) {

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

            }

        });
    }
}
