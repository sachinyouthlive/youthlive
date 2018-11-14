package com.app.youthlive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.youthlive.Activitys.Diamond_purchase_history_Activity;
import com.app.youthlive.walletPOJO.walletBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Diamonds extends Fragment {

    TextView amount, history, googlePay;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diamond_layout, container, false);


        amount = view.findViewById(R.id.textView6);
        history = view.findViewById(R.id.history);
        googlePay = view.findViewById(R.id.textView11);
        progress = view.findViewById(R.id.progressBar6);

        googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), BuyDiamonds.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), Diamond_purchase_history_Activity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDiamondData();
    }

    public void getDiamondData() {
        progress.setVisibility(View.VISIBLE);


        final bean b = (bean) getContext().getApplicationContext();

        Call<walletBean> call = b.getRetrofit().getWalletData(SharePreferenceUtils.getInstance().getString("userId"));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<walletBean>() {
            @Override
            public void onResponse(Call<walletBean> call, Response<walletBean> response) {

                try {
                    if (!response.body().getData().getDiamond().isEmpty()) {
                        //  Toast.makeText(BuyDiamonds.this, "Purchase done", Toast.LENGTH_SHORT).show();
                        amount.setText(response.body().getData().getDiamond());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<walletBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }

        });
    }


}
