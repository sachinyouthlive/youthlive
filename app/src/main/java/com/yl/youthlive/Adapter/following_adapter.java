package com.yl.youthlive.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.Activitys.FollowingActivity;
import com.yl.youthlive.Activitys.MessaageActivity;
import com.yl.youthlive.Activitys.PersonalInfo;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.followListPOJO.Datum;
import com.yl.youthlive.followPOJO.followBean;
import com.yl.youthlive.sendMessagePOJO.sendMessageBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class following_adapter extends RecyclerView.Adapter<following_adapter.followingadapter>{

    Context context;
    List<Datum> list = new ArrayList<>();
    FollowingActivity con;


    public following_adapter(Context context , List<Datum> list)
    {
        this.list = list;
        this.context=context;
    }


    @Override
    public followingadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        con = (FollowingActivity)context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.following_recycler , parent , false);
        return new followingadapter(view);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final followingadapter holder, int position) {

        final Datum item = list.get(position);

        if (!item.getUserImage().isEmpty()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(item.getUserImage(), holder.image);

        }

        holder.name.setText(item.getUserName());
        holder.youthId.setText("Youthlive Id: " + item.getYouthLiveId());

        holder.relative_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = item.getUserId();
                Intent intent = new Intent(context, PersonalInfo.class);
                intent.putExtra("userId", id);
                context.startActivity(intent);

            }
        });
        // loading follow unfollow data check
        final bean b = (bean) context.getApplicationContext();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())

                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<followBean> call = cr.followcheck(b.userId, item.getUserId());

        call.enqueue(new Callback<followBean>() {
            @Override
            public void onResponse(Call<followBean> call, Response<followBean> response) {

                try {
                    //  Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.body().getMessage().equals("Following")) {
                        holder.unfollow.setText("UNFOLLOW");

                    }
                    if (response.body().getMessage().equals("Not Following")) {
                        holder.unfollow.setText("FOLLOW");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    // Toast.makeText(TimelineProfile.this , "Some Error Occurred, Please try again follow" , Toast.LENGTH_SHORT).show();
                }


                con.progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<followBean> call, Throwable t) {

                con.progress.setVisibility(View.GONE);

            }
        });


        //



        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.send_message_dialog);
                dialog.setCancelable(true);
                dialog.show();

                final EditText comment = (EditText) dialog.findViewById(R.id.message);
                final ProgressBar bar = (ProgressBar) dialog.findViewById(R.id.progress);
                Button submit = (Button) dialog.findViewById(R.id.send);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        bar.setVisibility(View.VISIBLE);

                        String comm = comment.getText().toString();

                        if (comm.length() > 0) {

                            //    progress.setVisibility(View.VISIBLE);

                            bean b = (bean) context.getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                            Call<sendMessageBean> call = cr.sendMessage(b.userId, item.getUserId(), comm);

                            call.enqueue(new Callback<sendMessageBean>() {
                                @Override
                                public void onResponse(Call<sendMessageBean> call, Response<sendMessageBean> response) {


                                    dialog.dismiss();


                                    Intent intent = new Intent(context, MessaageActivity.class);
                                    context.startActivity(intent);


                                    bar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(Call<sendMessageBean> call, Throwable t) {

                                    bar.setVisibility(View.GONE);

                                }
                            });

                        }

                    }
                });





            }
        });


        holder.unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                con.progress.setVisibility(View.VISIBLE);

                final bean b = (bean) context.getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<followBean> call = cr.follow(b.userId , item.getUserId());

                call.enqueue(new Callback<followBean>() {
                    @Override
                    public void onResponse(Call<followBean> call, Response<followBean> response) {

                        //  Toast.makeText(context , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                        con.progress.setVisibility(View.GONE);

                        con.loadData();

                    }

                    @Override
                    public void onFailure(Call<followBean> call, Throwable t) {

                        con.progress.setVisibility(View.GONE);
                        Log.d("asdad" , t.toString());

                    }
                });



            }
        });

    }




    public void setGridData(List<Datum> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class followingadapter extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView name , youthId , beans;
        Button unfollow;
        Button message;
        RelativeLayout relative_following;


        public followingadapter(View itemView) {
        super(itemView);

        image = (CircleImageView)itemView.findViewById(R.id.image);
        name = (TextView)itemView.findViewById(R.id.name);
        youthId = (TextView)itemView.findViewById(R.id.yid);
        beans = (TextView)itemView.findViewById(R.id.beans);
        message = (Button)itemView.findViewById(R.id.message);

        unfollow = (Button)itemView.findViewById(R.id.unfollow);
            relative_following = itemView.findViewById(R.id.relative_follow);

    }
}
}
