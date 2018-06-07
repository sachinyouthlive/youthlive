package com.yl.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yl.youthlive.Activitys.PersonalInfo;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.addEducationPOJO.addEducationBean;
import com.yl.youthlive.editEducationPOJO.editEducationBean;
import com.yl.youthlive.loginResponsePOJO.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TBX on 12/20/2017.
 */

public class Education2 extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<com.yl.youthlive.timelineProfilePOJO.Education> list;
    EducationAdapter adapter;
    FloatingActionButton add;
    String userId;

    TimelineProfile per;

    public void setData(List<com.yl.youthlive.timelineProfilePOJO.Education> list)
    {
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.education_layout, container, false);


        per = (TimelineProfile)getActivity();

        userId = getArguments().getString("userId");



        //list = new ArrayList<>();
        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new EducationAdapter(getActivity() , list);
        add = view.findViewById(R.id.add);


        bean b1 = (bean)getContext().getApplicationContext();

        if (!Objects.equals(userId, b1.userId))
        {
            add.setVisibility(View.GONE);
        }
        else
        {
            add.setVisibility(View.VISIBLE);
        }


        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);

        adapter.setGridData(list);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.add_education_popup);
                dialog.show();

                final EditText edu = dialog.findViewById(R.id.title);
                final EditText year = dialog.findViewById(R.id.year);
                Button submit = dialog.findViewById(R.id.submit);
                final ProgressBar bar = dialog.findViewById(R.id.progress);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String e = edu.getText().toString();
                        String y = year.getText().toString();

                        if (e.length() > 0 )
                        {

                            if (y.length() > 0)
                            {


                                bar.setVisibility(View.VISIBLE);

                                bean b = (bean) getContext().getApplicationContext();

                                final Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(b.BASE_URL)
                                        .addConverterFactory(ScalarsConverterFactory.create())
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();

                                final AllAPIs cr = retrofit.create(AllAPIs.class);


                                Call<addEducationBean> call = cr.addEducation(userId , e , y);

                                call.enqueue(new Callback<addEducationBean>() {
                                    @Override
                                    public void onResponse(Call<addEducationBean> call, Response<addEducationBean> response) {

                                        dialog.dismiss();
                                        //loadData();

                                        per.loadData();


                                        bar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onFailure(Call<addEducationBean> call, Throwable t) {
                                        bar.setVisibility(View.GONE);
                                    }
                                });


                            }
                            else
                            {
                                Toast.makeText(getContext() , "Invalid Year" , Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(getContext() , "Invalid Title" , Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


        return view;
    }

    public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {
        List<com.yl.youthlive.timelineProfilePOJO.Education> list = new ArrayList<>();
        Context context;

        public EducationAdapter(Context context, List<com.yl.youthlive.timelineProfilePOJO.Education> list) {
            this.list = list;
            this.context = context;
        }

        public void setGridData(List<com.yl.youthlive.timelineProfilePOJO.Education> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.education_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final com.yl.youthlive.timelineProfilePOJO.Education item = list.get(position);

            holder.title.setText(item.getSchoolTitle());
            holder.time.setText(item.getTime());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.edit_education_popup);
                    dialog.show();

                    final EditText edu = dialog.findViewById(R.id.title);
                    final EditText year = dialog.findViewById(R.id.year);
                    Button submit = dialog.findViewById(R.id.submit);
                    final ProgressBar bar = dialog.findViewById(R.id.progress);
                    ImageButton delete = dialog.findViewById(R.id.delete);


                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            bar.setVisibility(View.VISIBLE);

                            bean b = (bean) getContext().getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);


                            Call<editEducationBean> call = cr.deleteEducation(userId , item.getEducationId());

                            call.enqueue(new Callback<editEducationBean>() {
                                @Override
                                public void onResponse(Call<editEducationBean> call, Response<editEducationBean> response) {

                                    dialog.dismiss();
                                    //loadData();

                                    per.loadData();


                                    bar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(Call<editEducationBean> call, Throwable t) {
                                    bar.setVisibility(View.GONE);
                                }
                            });



                        }
                    });


                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String e = edu.getText().toString();
                            String y = year.getText().toString();

                            if (e.length() > 0 )
                            {

                                if (y.length() > 0)
                                {


                                    bar.setVisibility(View.VISIBLE);

                                    bean b = (bean) getContext().getApplicationContext();

                                    final Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(b.BASE_URL)
                                            .addConverterFactory(ScalarsConverterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                                    Call<editEducationBean> call = cr.editEducation(userId , item.getEducationId() , e , y);

                                    call.enqueue(new Callback<editEducationBean>() {
                                        @Override
                                        public void onResponse(Call<editEducationBean> call, Response<editEducationBean> response) {

                                            dialog.dismiss();
                                            //loadData();

                                            per.loadData();


                                            bar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onFailure(Call<editEducationBean> call, Throwable t) {
                                            bar.setVisibility(View.GONE);
                                        }
                                    });


                                }
                                else
                                {
                                    Toast.makeText(getContext() , "Invalid Year" , Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(getContext() , "Invalid Title" , Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            });



        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView title , time;

            public ViewHolder(View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.title);
                time = itemView.findViewById(R.id.time);

            }
        }

    }

}
