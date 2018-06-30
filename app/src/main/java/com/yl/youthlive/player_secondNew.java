package com.yl.youthlive;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class player_secondNew extends Fragment {
    private MyPlayerInterface mCallback;
    ImageButton close;
    PlayerActivityNew plactivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_pager , container , false);

        plactivity = (PlayerActivityNew) getActivity();

        close = (ImageButton)view.findViewById(R.id.close);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                plactivity.finish();
                mCallback.closeConnections();

            }
        });



        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MyPlayerInterface) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }
    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }
}
