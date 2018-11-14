package com.app.youthlive;

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

import com.app.youthlive.vlogListPopularPOJO.vlogListPopularBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class popularVolg extends Fragment {

    RecyclerView recyclerView;
    GridLayoutManager manager;
    PopularAdapter adapter;
    List<com.app.youthlive.vlogListPopularPOJO.Datum> list;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hot, container, false);

        progress = view.findViewById(R.id.progress);

        list = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        manager = new GridLayoutManager(getContext(), 2);
        adapter = new PopularAdapter(getContext(), list);
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
        final bean b = (bean) Objects.requireNonNull(getContext()).getApplicationContext();

        Call<vlogListPopularBean> call = b.getRetrofit().getVlogListpopular(SharePreferenceUtils.getInstance().getString("userId"));

        Log.d("userId", SharePreferenceUtils.getInstance().getString("userId"));

        call.enqueue(new Callback<vlogListPopularBean>() {
            @Override
            public void onResponse(Call<vlogListPopularBean> call, Response<vlogListPopularBean> response) {

                try {
                    adapter.setGridData(response.body().getData());
                    b.plist = response.body().getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<vlogListPopularBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });
    }
}
