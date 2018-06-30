package com.yl.youthlive;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Diamonds extends Fragment {

    TextView amount , history , googlePay;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diamond_layout , container , false);


        amount = view.findViewById(R.id.textView6);
        history = view.findViewById(R.id.textView8);
        googlePay = view.findViewById(R.id.textView11);
        progress = view.findViewById(R.id.progressBar6);




        googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext() , BuyDiamonds.class);
                startActivity(intent);

            }
        });


        





        return view;
    }
}
