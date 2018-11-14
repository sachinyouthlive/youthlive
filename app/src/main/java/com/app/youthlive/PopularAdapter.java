package com.app.youthlive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.youthlive.vlogListPopularPOJO.Datum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    Context context;
    List<Datum> list = new ArrayList<>();


    public PopularAdapter(Context context, List<Datum> list) {
        this.context = context;
        this.list = list;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public void setGridData(List<Datum> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_list_model, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //holder.setIsRecyclable(false);
        final Datum item = list.get(position);
        holder.title.setText(item.getTimelineName());
        holder.likes.setText(item.getLikesCount());

        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(item.getTimelineProfileImage(), holder.profile, options);


        if (Objects.equals(item.getIsLiked(), "1")) {

            holder.likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);

        } else {
            holder.likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.heart, 0, 0, 0);
        }


        loader.displayImage(item.getThumbURL(), holder.image, options);


        //Glide.with(context).load(item.getTimelineProfileImage()).into(holder.profile);
        //Glide.with(context).load(item.getVideoURL()).into(holder.image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) context;
                Intent intent = new Intent(context, VerticalFragmentVodPopularActivity.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("videoId", item.getVideoId());
                intent.putExtra("url", item.getVideoURL());
                intent.putExtra("thumb", item.getThumbURL());
                intent.putExtra("userName", item.getTimelineName());


                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, likes;
        ImageView image;
        CircleImageView profile;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            likes = (TextView) itemView.findViewById(R.id.likes);
            image = (ImageView) itemView.findViewById(R.id.image);
            profile = (CircleImageView) itemView.findViewById(R.id.profile);
        }
    }
}
