package com.app.youthlive;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.app.youthlive.endLivePOJO.endLiveBean;
import com.app.youthlive.internetConnectivity.ConnectivityReceiver;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TBX on 11/22/2017.
 */

public class GoLiveFrag extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    Button goLive;
    CircleImageView profile;
    //CameraView cameraPreview;
    ImageView image;

    SharedPreferences offlinePref;
    SharedPreferences.Editor offlineEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_go_live, container, false);


        offlinePref = Objects.requireNonNull(getContext()).getSharedPreferences("offline", Context.MODE_PRIVATE);
        offlineEdit = offlinePref.edit();


/*


        cameraPreview.setFacing(Facing.FRONT);
*/

        image = view.findViewById(R.id.preview);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));


        profile = view.findViewById(R.id.profile);
        goLive = view.findViewById(R.id.golive);


        goLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                goLive.setClickable(false);
                goLive.setEnabled(false);


                String offline = offlinePref.getString("offline", "");

                final String liveId = offlinePref.getString("liveId", "");

                if (offline.length() > 0 && liveId.length() > 0) {


                    final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.offline_sync_dialog);
                    dialog.show();


                    bean b = (bean) getActivity().getApplicationContext();

                    Call<endLiveBean> call = b.getRetrofit().syncLive(offline, liveId);

                    call.enqueue(new Callback<endLiveBean>() {
                        @Override
                        public void onResponse(@NonNull Call<endLiveBean> call, @NonNull Response<endLiveBean> response) {


                            if (response.body() != null && response.body().getStatus().equals("1")) {

                                offlineEdit.remove("offline");
                                offlineEdit.remove("liveId");
                                offlineEdit.apply();

                                dialog.dismiss();


                                Intent intent = new Intent(getContext(), VideoBroadcaster.class);
                                //Intent intent = new Intent(getContext() , LiveScreenNew.class);
                                //Intent intent = new Intent(getContext() , WowzaLive.class);
                                //intent.putExtra("title" , title.getText().toString());
                                startActivityForResult(intent, 112);

                                goLive.setClickable(true);
                                goLive.setEnabled(true);

                            }


                        }

                        @Override
                        public void onFailure(@NonNull Call<endLiveBean> call, @NonNull Throwable t) {

                            goLive.setClickable(true);
                            goLive.setEnabled(true);

                        }
                    });


                } else {
                    Intent intent = new Intent(getContext(), VideoBroadcaster.class);
                    //Intent intent = new Intent(getContext() , LiveScreenNew.class);
                    //Intent intent = new Intent(getContext() , WowzaLive.class);
                    //intent.putExtra("title" , title.getText().toString());
                    startActivityForResult(intent, 112);
                    goLive.setClickable(true);
                    goLive.setEnabled(true);

                }


            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);

        ImageLoader loader = ImageLoader.getInstance();

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

        loader.loadImage(SharePreferenceUtils.getInstance().getString("userImage"), options , new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("eeee", failReason.getCause().getMessage());
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                profile.setImageBitmap(loadedImage);

                try {
                    Blurry.with(getActivity()).from(loadedImage).into(image);
                } catch (Exception e) {

                    Log.d("eeee", e.toString());

                    e.printStackTrace();
                }


            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });


    }

    private void showalert(boolean isConnected) {
        if (!isConnected) {
            try {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                }
                builder.setTitle("NO INTERNET CONNECTION")
                        .setMessage("Please check your internet connection setting and click refresh")
                        .setPositiveButton(R.string.Refresh, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Reload current fragment
                                FragmentTransaction ft = null;
                                if (getFragmentManager() != null) {
                                    ft = getFragmentManager().beginTransaction();
                                }
                                if (ft != null) {
                                    ft.detach(GoLiveFrag.this).attach(GoLiveFrag.this).commit();
                                }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {


            String offline = offlinePref.getString("offline", "");

            final String liveId = offlinePref.getString("liveId", "");

            if (offline.length() > 0 && liveId.length() > 0) {


                final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.offline_sync_dialog);
                dialog.show();


                bean b = (bean) getActivity().getApplicationContext();

                Call<endLiveBean> call = b.getRetrofit().syncLive(offline, liveId);

                call.enqueue(new Callback<endLiveBean>() {
                    @Override
                    public void onResponse(@NonNull Call<endLiveBean> call, @NonNull Response<endLiveBean> response) {


                        if (response.body() != null && response.body().getStatus().equals("1")) {

                            offlineEdit.remove("offline");
                            offlineEdit.remove("liveId");
                            offlineEdit.apply();

                            dialog.dismiss();

                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<endLiveBean> call, @NonNull Throwable t) {

                    }
                });


            }


            final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.no_internet_dialog);
            dialog.show();

            Button close = dialog.findViewById(R.id.button10);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        }

    }
}
