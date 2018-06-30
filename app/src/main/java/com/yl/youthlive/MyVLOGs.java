package com.yl.youthlive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.deleteVLOGPOJO.deleteVLOGBean;
import com.yl.youthlive.vlogListPOJO.Datum;
import com.yl.youthlive.vlogListPOJO.vlogListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MyVLOGs extends Fragment {

    RecyclerView recyclerView;
    GridLayoutManager manager;
    ProgressBar progress;
    MyVLOGAdapter adapter;
    List<Datum> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot, container, false);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 2);

        list = new ArrayList<>();
        adapter = new MyVLOGAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {

        final bean b = (bean) getContext().getApplicationContext();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllAPIs cr = retrofit.create(AllAPIs.class);
        Call<vlogListBean> call = cr.getVlog(b.userId);

        Log.d("userId", b.userId);

        call.enqueue(new Callback<vlogListBean>() {
            @Override
            public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {
                adapter.setGridData(response.body().getData());
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<vlogListBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }


    class MyVLOGAdapter extends RecyclerView.Adapter<MyVLOGAdapter.MyViewHolder> {

        List<Datum> list = new ArrayList<>();
        Context context;

        public MyVLOGAdapter(Context context, List<Datum> list) {
            this.list = list;
            this.context = context;
        }

        public void setGridData(List<Datum> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.my_list_model, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            final Datum item = list.get(position);
            holder.likes.setText(item.getLikesCount());

            //Glide.with(context).load(item.getVideoURL()).into(holder.image);

            if (Objects.equals(item.getIsLiked(), "1"))
            {

                holder.likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_like , 0 , 0 , 0);

            }
            else
            {
                holder.likes.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.heart , 0 , 0 , 0);
            }

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(item.getThumbURL() , holder.image , options);


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SingleVideoActivity.class);
                    intent.putExtra("videoId", item.getVideoId());
                    intent.putExtra("url", item.getVideoURL());
                    intent.putExtra("thumb", item.getThumbURL());
                    context.startActivity(intent);
                }
            });

            holder.cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    progress.setVisibility(View.VISIBLE);


                    final bean b = (bean) context.getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    Call<deleteVLOGBean> call = cr.removeVideo(b.userId , item.getVideoId());

                    call.enqueue(new Callback<deleteVLOGBean>() {
                        @Override
                        public void onResponse(Call<deleteVLOGBean> call, Response<deleteVLOGBean> response) {

                            loadData();

                        }

                        @Override
                        public void onFailure(Call<deleteVLOGBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView likes;
            ImageView image;
            ImageButton cross, play;


            public MyViewHolder(View itemView) {
                super(itemView);

                likes = (TextView) itemView.findViewById(R.id.likes);
                image = (ImageView) itemView.findViewById(R.id.image);
                cross = (ImageButton) itemView.findViewById(R.id.cross);
                play = itemView.findViewById(R.id.play);

            }
        }

        public Bitmap retriveVideoFrameFromVideo(String videoPath)
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
    }

}
