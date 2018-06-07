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
import android.widget.Toast;

import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

public class secondfragNew extends Fragment {

    private MyInterface mCallback;
    ImageButton close;
    LiveScreenNew lvscreen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_pager , container , false);

        Toast.makeText(getActivity(), "secondfrag.java", Toast.LENGTH_SHORT).show();

        lvscreen = (LiveScreenNew) getActivity();

        close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();
                //finish();
              mCallback.closeConnections();

             //   lvscreen.closeConnection();


            }
        });


        return view;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MyInterface) context;

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
