package com.yl.youthlive.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.Activitys.FollowingActivity;
import com.yl.youthlive.Activitys.MessaageActivity;
import com.yl.youthlive.Activitys.PersonalInfo;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.R;
import com.yl.youthlive.bean;
import com.yl.youthlive.followListPOJO.Datum;
import com.yl.youthlive.followPOJO.Data;
import com.yl.youthlive.followPOJO.followBean;

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
    public void onBindViewHolder(followingadapter holder, int position) {

        final Datum item = list.get(position);

        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(item.getUserImage() , holder.image);
        holder.name.setText(item.getUserName());
        holder.youthId.setText("Youthlive Id: " + item.getYouthLiveId());



        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MessaageActivity.class);
                context.startActivity(intent);

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

                        Toast.makeText(context , response.body().getMessage() , Toast.LENGTH_SHORT).show();

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


        public followingadapter(View itemView) {
        super(itemView);

        image = (CircleImageView)itemView.findViewById(R.id.image);
        name = (TextView)itemView.findViewById(R.id.name);
        youthId = (TextView)itemView.findViewById(R.id.yid);
        beans = (TextView)itemView.findViewById(R.id.beans);
        message = (Button)itemView.findViewById(R.id.message);

        unfollow = (Button)itemView.findViewById(R.id.unfollow);

    }
}
}
