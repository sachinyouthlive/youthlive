package com.yl.youthlive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

public class secondfrag extends Fragment implements WZStatusCallback {


    ImageButton close;
    LiveScreen lvscreen;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_pager, container, false);

        lvscreen = (LiveScreen) getActivity();

        close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mBroadcaster.stopBroadcast();
                //finish();


                lvscreen.closeConnection();


            }
        });


        return view;

    }

    @Override
    public void onWZStatus(WZStatus wzStatus) {

    }

    @Override
    public void onWZError(WZStatus wzStatus) {

    }
}
