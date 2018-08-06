package com.yl.youthlive;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by TBX on 11/22/2017.
 */

public class GoLiveFrag extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    Button goLive;
    CircleImageView profile;
    //CameraView cameraPreview;
    ImageView image;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_go_live, container, false);


/*


        cameraPreview.setFacing(Facing.FRONT);
*/

        image = view.findViewById(R.id.preview);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));


        profile = (CircleImageView) view.findViewById(R.id.profile);
        goLive = (Button) view.findViewById(R.id.golive);


        bean b = (bean) getContext().getApplicationContext();

        ImageLoader loader = ImageLoader.getInstance();

        loader.loadImage(b.userImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                profile.setImageBitmap(loadedImage);

                Blurry.with(getContext()).from(loadedImage).into(image);

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        goLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), VideoBroadcaster.class);
                //Intent intent = new Intent(getContext() , LiveScreenNew.class);
                //Intent intent = new Intent(getContext() , WowzaLive.class);
                //intent.putExtra("title" , title.getText().toString());
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);


        try {

            //cameraPreview.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showalert(isConnected);
    }

    private void showalert(boolean isConnected) {
        if (isConnected) {

            //    message = "Good! Connected to Internet";
            //    color = Color.WHITE;
        } else {

            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(getActivity());
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Reload current fragment
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(GoLiveFrag.this).attach(GoLiveFrag.this).commit();
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
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        //cameraPreview.stop();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showalert(isConnected);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //cameraPreview.destroy();
    }
}
