package com.yl.youthlive;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.internetConnectivity.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Channel extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    RecyclerView grid;
    GridLayoutManager manager;
    //LiveAdapter adapter;
    LiveAdapter2 adapter2;
    ProgressBar progress;

    List<liveBean> list2;

    HomeActivity homeActivity;

    public void setHomeActivity(HomeActivity homeActivity)
    {
        this.homeActivity = homeActivity;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_layout, container, false);
        checkConnection();


        list2 = new ArrayList<>();
        grid = (RecyclerView) view.findViewById(R.id.grid);
        manager = new GridLayoutManager(getContext(), 2);

        progress = (ProgressBar) view.findViewById(R.id.progress);


        //adapter = new LiveAdapter(getContext(), list);
        adapter2 = new LiveAdapter2(getContext(), list2);

        grid.setAdapter(adapter2);
        grid.setLayoutManager(manager);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        // register connection status listener
        bean.getInstance().setConnectivityListener(this);


        progress.setVisibility(View.VISIBLE);

        final bean b = (bean) getContext().getApplicationContext();


/*
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
*/


        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                //.client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<List<liveBean>> call = cr.getLives2(SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<List<liveBean>>() {
            @Override
            public void onResponse(Call<List<liveBean>> call, Response<List<liveBean>> response) {

                List<liveBean> ll = new ArrayList<>();

                for (int i = 0 ; i < response.body().size() ; i++)
                {
                    if (!response.body().get(i).getType().equals("live"))
                    {
                        ll.add(response.body().get(i));
                    }
                }

                adapter2.setGridData(ll);

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<liveBean>> call, Throwable throwable) {
                progress.setVisibility(View.GONE);
                throwable.printStackTrace();
            }
        });


        /*progress.setVisibility(View.VISIBLE);



        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.3:8087/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);


        Call<String> call = cr.getEngineLiveList();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {


                Log.d("asdasd" , "data");


                list2.clear();

                try {

                    //for (int i = 0; i < response.body().getIncomingStreams().size(); i++) {
                        //if (!Objects.equals(response.body().get(i).getUserStatus(), "stopped")) {
                    //    list2.add(response.body().getIncomingStreams().get(i));
                        //}
                    //}

                    Log.d("response" , response.body());

                    try {


                        adapter2.setGridData(list2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }catch (Exception e)
                {

                    Toast.makeText(getContext() , "Some Error Occurred, Please try again" , Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }




                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);

                Log.d("asdasd" , t.toString());

            }
        });
*/


    }
    ////////////////////internet connectivity check///////////////
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showalert(isConnected);
    }
    private void showalert(boolean isConnected) {
        if (isConnected) {

            // Toast.makeText(getActivity(), "Good! Connected to Internet", Toast.LENGTH_SHORT).show();
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
                                ft.detach(Channel.this).attach(Channel.this).commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            catch(Exception e)
            {
                Log.d("TAG", "Show Dialog: "+e.getMessage());
            }
        }

    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showalert(isConnected);

    }


    public class LiveAdapter2 extends RecyclerView.Adapter<LiveAdapter2.ViewHolder> {

        Context context;
        List<liveBean> list = new ArrayList<>();

        public LiveAdapter2(Context context, List<liveBean> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<liveBean> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.live_list_model, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            holder.setIsRecyclable(false);

            final liveBean item = list.get(position);

            holder.title.setText(item.getTitle());

            DisplayImageOptions options = new DisplayImageOptions.Builder().resetViewBeforeLoading(false).cacheOnDisk(true).cacheInMemory(true).build();

            ImageLoader loader = ImageLoader.getInstance();

            final bean b = (bean)context.getApplicationContext();

            loader.displayImage(b.BASE_URL + item.getUserImage() , holder.image , options);

            int lu = Integer.parseInt(item.getLiveUsers());

            if (item.getType().equals("live"))
            {
                holder.viewCount.setText(String.valueOf(lu));
            }
            else
            {
                holder.viewCount.setText(String.valueOf(lu + 300));
            }



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*
                    if (item.getType().equals("live"))
                    {
*/

                    Intent intent = new Intent(context, YoutubePlayer.class);
                    intent.putExtra("uri", item.getChannelUrl());
                    intent.putExtra("liveId", item.getLiveId());
                    intent.putExtra("pic",b.BASE_URL + item.getUserImage());
                    intent.putExtra("timelineId", String.valueOf(item.getUserId()));
                    startActivity(intent);


/*

                    b.frag = true;

                    FragmentTransaction ft = ((HomeActivity)context).getSupportFragmentManager().beginTransaction();
                    VerticalFragment frag1 = new VerticalFragment();
                    frag1.setHomeActivity(homeActivity);
                    frag1.setList(list , position);
                    ft.replace(R.id.replace2, frag1);
                    ft.addToBackStack(null);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                    ft.commit();
*/



                        /*
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("uri", item.getLiveId());
                        intent.putExtra("liveId", item.getLiveId());
                        intent.putExtra("pic",b.BASE_URL + item.getUserImage());
                        intent.putExtra("timelineId", String.valueOf(item.getUserId()));
                        startActivity(intent);*/

                 /*   }
                    else
                    {


                    }
*/

                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView title, viewCount;

            public ViewHolder(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.image);
                title = (TextView) itemView.findViewById(R.id.title);
                viewCount = (TextView) itemView.findViewById(R.id.view_count);

            }
        }
    }


}
