package com.yl.youthlive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import java.util.ArrayList;
import java.util.List;

public class BuyDiamonds extends AppCompatActivity implements BillingProcessor.IBillingHandler{

    BillingProcessor bp;
    ArrayList<String> ids;
    List<SkuDetails> skus;

    String TAG = "ggooggllee";

    Toolbar toolbar;
    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;

    DiamomdsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_diamonds);



        ids = new ArrayList<>();
        skus = new ArrayList<>();


        ids.add("diamond_70");
        ids.add("diamond_570");
        ids.add("diamond_970");
        ids.add("diamond_4000");
        ids.add("diamond_7000");
        ids.add("test1");

        String liscense = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgyeBlAF+Tb5RcId3Y9sAnK7EoGEklDr24FByrgxwhQNsIOkhYfDH+KW4OGxqR47D+RjH3uHBgtKjD62qgvSsqiJR4KiHAq5gVZLZJ3nP0YDvnfWwyhg+t6FYnchGVGt2FbuNyw+XqPuZvoxUQmfB4qsIOQlbf9HI69uisnOZzuJ5b2VIVg3yIymF45jAm9+U5DdqP3vO7pHF4Y3yycOS6EIYs3VoZJ8JmJIOVHpFc//fxBaV4OKgKJij/28v5C94RRay55wHO0+ysW4fAQW52SxXX2vsQMGWYRmMzLs+N87PUixYJP96BbkUh4mkGFC1RMq8iUaeWLKIut74VhmCLQIDAQAB";




        toolbar = findViewById(R.id.toolbar);
        grid = findViewById(R.id.grid);
        manager = new GridLayoutManager(this , 1);
        progress = findViewById(R.id.progress);


        adapter = new DiamomdsAdapter(this , skus);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        progress.setVisibility(View.VISIBLE);
        bp = new BillingProcessor(this , liscense , this);
        //

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Buy Diamonds");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

        Log.d(TAG , "purchased: " + productId);

    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d(TAG , "history restored: ");
    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {
        Log.d(TAG , "initialized: ");

        progress.setVisibility(View.GONE);

        skus.add(bp.getPurchaseListingDetails(ids.get(0)));
        skus.add(bp.getPurchaseListingDetails(ids.get(1)));
        skus.add(bp.getPurchaseListingDetails(ids.get(2)));
        skus.add(bp.getPurchaseListingDetails(ids.get(3)));
        skus.add(bp.getPurchaseListingDetails(ids.get(4)));
        skus.add(bp.getPurchaseListingDetails(ids.get(5)));

/*
        skus = bp.getPurchaseListingDetails(ids);
        Log.d(TAG , String.valueOf(skus.size()));*/

        adapter.setGridData(skus);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    class DiamomdsAdapter extends RecyclerView.Adapter<DiamomdsAdapter.ViewHolder>
    {
        List<SkuDetails> list = new ArrayList<>();
        Context context;

        public DiamomdsAdapter(Context context , List<SkuDetails> list)
        {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<SkuDetails> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.diamond_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final SkuDetails item = list.get(position);

            holder.quantity.setText(item.description);
            holder.buy.setText(item.currency + " " + item.priceValue);


            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bp.purchase((Activity) context , item.productId);

                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            TextView quantity;
            Button buy;

            public ViewHolder(View itemView) {
                super(itemView);

                quantity = itemView.findViewById(R.id.textView17);
                buy = itemView.findViewById(R.id.button4);

            }
        }
    }


}
