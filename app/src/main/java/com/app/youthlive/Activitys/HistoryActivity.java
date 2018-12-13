package com.app.youthlive.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.app.youthlive.R;
import com.app.youthlive.SharePreferenceUtils;
import com.app.youthlive.bean;

import com.app.youthlive.giftHistoryPOJO.Information;
import com.app.youthlive.giftHistoryPOJO.giftHistoryBean;
import com.app.youthlive.internetConnectivity.ConnectivityReceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.youthlive.bean.getContext;

public class HistoryActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    diamondPHAdapter adapter;
    ProgressBar progress;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    List<Information> list = new ArrayList<>();
    Toolbar toolbar;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        checkConnection();

        type = getIntent().getStringExtra("type");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle("History");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        progress = findViewById(R.id.progress);

        recyclerView = findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new diamondPHAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        Typeface typeFace = Typeface.MONOSPACE;
        ((TextView) toolbar.getChildAt(0)).setTypeface(typeFace);



    }

    @Override
    protected void onResume() {
        super.onResume();
        bean.getInstance().setConnectivityListener(this);
        loadData();
    }

    public void loadData() {
        progress.setVisibility(View.VISIBLE);
        final LinearLayout linearLayout = findViewById(R.id.nobroadcast);


        final bean b = (bean) getContext().getApplicationContext();
        Call<giftHistoryBean> call = b.getRetrofit().getgiftshistory(Integer.valueOf(SharePreferenceUtils.getInstance().getString("userId")));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<giftHistoryBean>() {
            @Override
            public void onResponse(Call<giftHistoryBean> call, Response<giftHistoryBean> response) {

                try {
                    if (!response.body().getInformation().isEmpty()) {
                        adapter.setGridData(response.body().getInformation());
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<giftHistoryBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }

        });
    }

    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            // Toast.makeText(this, "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {
            //  Toast.makeText(this, "Sorry! Not connected to internet", Toast.LENGTH_SHORT).show();
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } catch (Exception e) {
                Log.d("TAG", "Show Dialog: " + e.getMessage());
            }
            //      message = "Sorry! Not connected to internet";
            //     color = Color.RED;
        }

       /* Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
        */
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }


    public class diamondPHAdapter extends RecyclerView.Adapter<diamondPHAdapter.MyViewHolder> {

        Context context;
        List<Information> list = new ArrayList<>();


        public diamondPHAdapter(Context context, List<Information> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<Information> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gifts_history_model, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Information item = list.get(position);

            final bean b = (bean) context.getApplicationContext();

            holder.ordernotxt.setText(item.getPName());

            holder.amount.setText(b.names[Integer.parseInt(item.getGiftId()) - 1]);


            holder.coins.setText("  " + Integer.parseInt(b.diamonds[Integer.parseInt(item.getGiftId()) - 1]) / 4 + " Coins");


            Drawable drawable = context.getResources().getDrawable(b.gifts[Integer.parseInt(item.getGiftId()) - 1]);

            drawable.setBounds(0, 0, 40, 40);

            int selectionCursor;
            selectionCursor = holder.amount.getText().length();

            SpannableStringBuilder builder = new SpannableStringBuilder(". " + holder.amount.getText());
            builder.setSpan(new ImageSpan(drawable), 0 , 1 , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.amount.setText(builder);


            String onlytime = item.getCreatedDate();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = dateFormatter.parse(onlytime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

// Get time from date
            SimpleDateFormat timeFormatter = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");
            String displayValue = timeFormatter.format(date).toUpperCase();
            holder.date.setText(displayValue);



            if (type.equals("gift"))
            {
                holder.amount.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.amount.setVisibility(View.GONE);
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ordernotxt, date, amount , coins;

            public MyViewHolder(View itemView) {
                super(itemView);
                amount = itemView.findViewById(R.id.ordercreated);
                ordernotxt = itemView.findViewById(R.id.ordernotxt);
                date = itemView.findViewById(R.id.date);
                coins = itemView.findViewById(R.id.coins);
            }
        }

    }


}
