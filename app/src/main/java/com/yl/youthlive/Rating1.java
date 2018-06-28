package com.yl.youthlive;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.GetRankingPOJO.Datum;
import com.yl.youthlive.GetRankingPOJO.RankingBean;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.followPOJO.followBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by USER on 12/18/2017.
 */

public class Rating1 extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    RankingAdapter adapter;
    List<Datum> list;
    ProgressBar bar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rating1 , container , false);

        grid = view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext() , 1);

        bar = view.findViewById(R.id.progress);

        list = new ArrayList<>();

        adapter = new RankingAdapter(getContext() , list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        bar.setVisibility(View.VISIBLE);

        bean b = (bean)getContext(). getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<RankingBean> call = cr.ranking(b.userId , "hourly");

        call.enqueue(new Callback<RankingBean>() {
            @Override
            public void onResponse(Call<RankingBean> call, Response<RankingBean> response) {

                adapter.setgrid(response.body().getData());

                bar.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<RankingBean> call, Throwable t) {

                bar.setVisibility(View.GONE);

            }
        });

        return view;
    }



    public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

        Context context;

        List<Datum>list = new ArrayList<>();

        public RankingAdapter(Context context ,  List<Datum>list){

            this.context = context;
            this.list = list;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(context).inflate(R.layout.tabratting_adapterlayout , parent , false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {


            final Datum item = list.get(position);


            holder.name.setText(item.getUserName());
            holder.beans.setText("Beans - " + item.getBeans());
            holder.change.setText(String.valueOf(position + 1 ));


            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                    .cacheOnDisc(true).resetViewBeforeLoading(false).build();
            if (!item.getUserImage().isEmpty()) {
                ImageLoader loader = ImageLoader.getInstance();
                loader.displayImage(item.getUserImage(), holder.image, options);
            }
            holder.follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    bar.setVisibility(View.VISIBLE);

                    final bean b = (bean)context. getApplicationContext();

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

                            Drawable dr = getResources().getDrawable(R.drawable.tickmark);
                            Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
                            Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 20, 20, true));
// Set your new, scaled drawable "d"

                            Toast.makeText(getContext() , response.body().getMessage() , Toast.LENGTH_SHORT).show();
                            if (response.body().getMessage().equals("Follow Success")) {
                                holder.follow.setBackground(d);
                            }
                            if (response.body().getMessage().equals("Unfollow Success")) {
                                holder.follow.setBackgroundResource(R.drawable.add);
                            }


                            bar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<followBean> call, Throwable t) {

                            bar.setVisibility(View.GONE);

                        }
                    });




                }
            });



        }


        public void setgrid( List<Datum>list){

            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView beans, name, change;

            ImageView image, follow;


            public MyViewHolder(View itemView) {
                super(itemView);


                beans = itemView.findViewById(R.id.beans);
                name = itemView.findViewById(R.id.name);
                change = itemView.findViewById(R.id.change);
                image = itemView.findViewById(R.id.image);
                follow = itemView.findViewById(R.id.follow);
                follow.setBackgroundResource(R.drawable.add);

            }
        }
    }

}
