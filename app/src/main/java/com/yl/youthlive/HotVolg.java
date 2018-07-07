package com.yl.youthlive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.vlogListPOJO.vlogListBean;
import com.yl.youthlive.vlogsearchListPOJO.vlogsearchListBean;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class HotVolg extends Fragment {

    RecyclerView recyclerView;
    GridLayoutManager manager, manager2;
    HotAdapter adapter;
    HotAdapterSearch adapterSearch;
    List<com.yl.youthlive.vlogListPOJO.Datum> list;
    List<com.yl.youthlive.vlogsearchListPOJO.Datum> list2;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hot, container, false);

        progress = (ProgressBar) view.findViewById(R.id.progress);

        list = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 2);
        adapter = new HotAdapter(getContext(), list);
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
        progress.setVisibility(View.VISIBLE);
        final bean b = (bean) getContext().getApplicationContext();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllAPIs cr = retrofit.create(AllAPIs.class);
        Call<vlogListBean> call = cr.getVlogList(b.userId);

        Log.d("userId", b.userId);

        call.enqueue(new Callback<vlogListBean>() {
            @Override
            public void onResponse(Call<vlogListBean> call, Response<vlogListBean> response) {

                try {
                    adapter.setGridData(response.body().getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<vlogListBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }

    public void loadDataSearch() {
        progress.setVisibility(View.VISIBLE);
        final bean b = (bean) getContext().getApplicationContext();
        final Retrofit retrofitsearch = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final AllAPIs cr = retrofitsearch.create(AllAPIs.class);
        Call<vlogsearchListBean> call = cr.getVlogSearchList((b.userId), "sachin");

        Toast.makeText(getActivity(), b.userId + "loaddatasearch", Toast.LENGTH_SHORT).show();

        Log.d("userId", b.userId);

        call.enqueue(new Callback<vlogsearchListBean>() {
            @Override
            public void onResponse(Call<vlogsearchListBean> call, Response<vlogsearchListBean> response) {

                try {
                    adapterSearch.setGridData(response.body().getData());
                    Log.e("log hotvolg", response.body().getData().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<vlogsearchListBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }
}
