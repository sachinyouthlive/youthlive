package com.yl.youthlive;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.timelineProfilePOJO.timelineProfileBean;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BroadcasterProfileSheet extends BottomSheetDialogFragment {

    String timelineId;
    CircleImageView profile;
    TextView friends, following, fans;
    Button follow;
    TextView name, yid;
    ProgressBar progress;
    ImageButton timelineFollow;

    public void setData(String timelineId , ImageButton timelineFollow) {
        this.timelineId = timelineId;
        this.timelineFollow = timelineFollow;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme1);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.broadcaster_profile_layout, container, false);

        profile = view.findViewById(R.id.view11);
        friends = view.findViewById(R.id.friends);
        following = view.findViewById(R.id.following);
        fans = view.findViewById(R.id.fans);
        follow = view.findViewById(R.id.button17);
        name = view.findViewById(R.id.textView54);
        yid = view.findViewById(R.id.textView55);
        progress = view.findViewById(R.id.progressBar14);


        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                final bean b = (bean) getContext().getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<followBean> call = cr.follow(SharePreferenceUtils.getInstance().getString("userId"), timelineId);

                call.enqueue(new Callback<followBean>() {
                    @Override
                    public void onResponse(Call<followBean> call, Response<followBean> response) {

                        try {

                            if (response.body().getMessage().equals("Follow Success")) {
                                follow.setText("UNFOLLOW");
                                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                                timelineFollow.setVisibility(View.GONE);
                                // Toast.makeText(TimelineProfile.this, "You started to follow " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            }
                            if (response.body().getMessage().equals("Unfollow Success")) {
                                follow.setText("FOLLOW");
                                follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus_white, 0, 0, 0);
                                timelineFollow.setVisibility(View.VISIBLE);
                                //  Toast.makeText(TimelineProfile.this, "You Unfollowed " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();
                            }
                            loadData();


                        } catch (Exception e) {
                            e.printStackTrace();
                            // Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                        }


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<followBean> call, Throwable t) {

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
        loadData();
        loadfollowstatus(timelineId);
        followingstatus(timelineId);
    }

    public void loadfollowstatus(String userids) {
        final bean b = (bean) getActivity().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<followBean> call = cr.followcheck(SharePreferenceUtils.getInstance().getString("userId"), userids);

        call.enqueue(new Callback<followBean>() {
            @Override
            public void onResponse(Call<followBean> call, Response<followBean> response) {

                try {


                    // Toast.makeText(TimelineProfile.this , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                    if (response.body().getMessage().equals("Following")) {
                        follow.setText("UNFOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                        //  Toast.makeText(TimelineProfile.this, "You started to follow " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();

                    }
                    if (response.body().getMessage().equals("Not Following")) {
                        follow.setText("FOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus, 0, 0, 0);
                        //Toast.makeText(TimelineProfile.this, "You started to notfollow " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText(TimelineProfile.this, "Some Error Occurred, Please try again follow", Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<followBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }

    public void followingstatus(String userids) {
        final bean b = (bean) getActivity().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<followBean> call = cr.followcheck(SharePreferenceUtils.getInstance().getString("userId"), userids);

        call.enqueue(new Callback<followBean>() {
            @Override
            public void onResponse(Call<followBean> call, Response<followBean> response) {

                try {

                    if (response.body().getMessage().equals("Following")) {
                        follow.setText("UNFOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.minus, 0, 0, 0);
                        //  Toast.makeText(TimelineProfile.this, "You started to following " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().getMessage().equals("Not Following")) {
                        follow.setText("FOLLOW");
                        follow.setCompoundDrawablesWithIntrinsicBounds(R.drawable.plus, 0, 0, 0);
                        //  Toast.makeText(TimelineProfile.this, "You started to notfollowing " + toolbar.getTitle().toString(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //  Toast.makeText(TimelineProfile.this, "Some Error Occurred, Please try again following", Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<followBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });
    }


    public void loadData() {


        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getActivity().getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<timelineProfileBean> call = cr.getProfile2(SharePreferenceUtils.getInstance().getString("userId"), timelineId);

        call.enqueue(new retrofit2.Callback<timelineProfileBean>() {
            @Override
            public void onResponse(Call<timelineProfileBean> call, retrofit2.Response<timelineProfileBean> response) {

                try {
                    if (Objects.equals(response.body().getStatus(), "1")) {


                        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
                        ImageLoader loader = ImageLoader.getInstance();
                        loader.displayImage(response.body().getData().getUserImage(), profile, options);

                        name.setText(response.body().getData().getUserName());
                        yid.setText(response.body().getData().getYouthLiveId());

                        fans.setText(String.valueOf(response.body().getData().getFans()));
                        following.setText(String.valueOf(response.body().getData().getFollowings()));
                        friends.setText(String.valueOf(response.body().getData().getFriends()));


                        if (response.body().getData().getUserId().equals(SharePreferenceUtils.getInstance().getString("userId"))) {
                            follow.setVisibility(View.GONE);
                            //messgae.setVisibility(View.GONE);
                        } else {

                            follow.setVisibility(View.VISIBLE);
                        }

                        /*if (Objects.equals(response.body().getData().getFriendStatus().getFollow(), "true")) {
                            if (!SharePreferenceUtils.getInstance().getString("userId").equals(userid)) {
                                messgae.setVisibility(View.VISIBLE);
                            }
                        } else {
                            messgae.setVisibility(View.GONE);
                        }*/


                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                }


                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<timelineProfileBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }

}
