package com.app.youthlive;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.youthlive.followPOJO.followBean;
import com.app.youthlive.getIpdatedPOJO.Comment;
import com.app.youthlive.requestConnectionPOJO.requestConnectionBean;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MuteSlide extends BottomSheetDialogFragment {

    CircleImageView profile;
    TextView username , ylid;

    ImageButton mute , follow , connect;
    TextView muteText , followText , connectText;

    ProgressBar progress;

    String name , uid , liveId , thumbPic1;
    Boolean isConnection;
    Comment item;

    public void setData(String name , String uid , Boolean isConnection , String liveId , String thumbPic1 , Comment item)
    {
        this.name = name;
        this.uid = uid;
        this.isConnection = isConnection;
        this.liveId = liveId;
        this.thumbPic1 = thumbPic1;
        this.item = item;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme1);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mute_layout , container , false);

        profile = view.findViewById(R.id.view11);
        username = view.findViewById(R.id.textView54);
        ylid = view.findViewById(R.id.textView55);
        mute = view.findViewById(R.id.imageButton12);
        follow = view.findViewById(R.id.imageButton13);
        connect = view.findViewById(R.id.imageButton14);
        muteText = view.findViewById(R.id.textView56);
        followText = view.findViewById(R.id.textView57);
        connectText = view.findViewById(R.id.textView58);
        progress = view.findViewById(R.id.progressBar);


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();

        loader.displayImage(SharePreferenceUtils.getInstance().getString("userImage"), profile , options);

        username.setText(name);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) Objects.requireNonNull(getContext()).getApplicationContext();


                retrofit2.Call<followBean> call = b.getRetrofit().follow(SharePreferenceUtils.getInstance().getString("userId"), uid);

                call.enqueue(new retrofit2.Callback<followBean>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<followBean> call, @NonNull retrofit2.Response<followBean> response) {

                        if (response.body() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<followBean> call, @NonNull Throwable t) {

                        progress.setVisibility(View.GONE);

                    }
                });


            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isConnection) {
                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) Objects.requireNonNull(getContext()).getApplicationContext();


                    Call<requestConnectionBean> call = b.getRetrofit().requestConnection(liveId, SharePreferenceUtils.getInstance().getString("userId"), uid);

                    call.enqueue(new Callback<requestConnectionBean>() {
                        @Override
                        public void onResponse(@NonNull Call<requestConnectionBean> call, @NonNull retrofit2.Response<requestConnectionBean> response) {

                            thumbPic1 = item.getUserImage().replace("\"", "");


                            isConnection = true;


                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(@NonNull Call<requestConnectionBean> call, @NonNull Throwable t) {
                            thumbPic1 = item.getUserImage();
                            isConnection = false;
                            progress.setVisibility(View.GONE);
                            Log.d("asdasdasdas", t.toString());
                        }
                    });


                } else {
                    Toast.makeText(getContext(), "You don't have any more room left", Toast.LENGTH_SHORT).show();
                }


            }
        });


        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) Objects.requireNonNull(getContext()).getApplicationContext();

                Call<String> call = b.getRetrofit().mute(name , liveId , uid);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {

                        Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();

                        progress.setVisibility(View.GONE);


                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        if (isConnection)
        {
            connect.setEnabled(false);
            connect.setClickable(false);
            connectText.setText("No Room");
        }
        else
        {
            connect.setEnabled(true);
            connect.setClickable(true);
            connectText.setText("Connect");
        }

        if (item.getFriendStatus().getFollow().equals("true"))
        {
            follow.setEnabled(false);
            follow.setClickable(false);
            followText.setText("Following");
        }
        else
        {
            follow.setEnabled(true);
            follow.setClickable(true);
            followText.setText("Follow");
        }

    }
}
