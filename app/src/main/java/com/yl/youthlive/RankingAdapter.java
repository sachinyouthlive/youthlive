package com.yl.youthlive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.GetRankingPOJO.Datum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 12/18/2017.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.MyViewHolder> {

    Context context;

    List<Datum> list = new ArrayList<>();

    public RankingAdapter(Context context, List<Datum> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.tabratting_adapterlayout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        Datum item = list.get(position);


        holder.name.setText(item.getUserName());
        holder.beans.setText(item.getBeans());
        holder.change.setText(String.valueOf(position + 1) + ".");


        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(false).build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(item.getUserImage(), holder.image, options);


        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }


    public void setgrid(List<Datum> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView beans, name, change, follow;

        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);


            beans = itemView.findViewById(R.id.beans);
            name = itemView.findViewById(R.id.name);
            change = itemView.findViewById(R.id.change);
            image = itemView.findViewById(R.id.image);
            follow = itemView.findViewById(R.id.follow);

        }
    }
}
