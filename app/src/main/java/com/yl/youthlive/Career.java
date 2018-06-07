package com.yl.youthlive;

import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.yl.youthlive.addCareerPOJO.addCareerBean;
import com.yl.youthlive.deleteCareerPOJO.deleteCareerBean;
import com.yl.youthlive.editCareerPOJO.editCareerBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.microedition.khronos.egl.EGLDisplay;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by TBX on 11/23/2017.
 */

public class Career extends Fragment {

    RecyclerView grid;
    GridLayoutManager manager;
    ProgressBar progress;
    List<com.yl.youthlive.loginResponsePOJO.Career> list;
    EducationAdapter adapter;
    FloatingActionButton add;
    PersonalInfo per;
    String userId;

    public void setData(List<com.yl.youthlive.loginResponsePOJO.Career> list)
    {
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.education_layout, container, false);
        Toast.makeText(getActivity(), "career.java", Toast.LENGTH_SHORT).show();

        //list = new ArrayList<>();

        per = (PersonalInfo)getActivity();

        userId = getArguments().getString("userId");

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);
        manager = new GridLayoutManager(getContext(), 1);
        adapter = new EducationAdapter(getContext() , list);
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
                dialog.setContentView(R.layout.add_career_popup);
                dialog.show();


                final EditText title = dialog.findViewById(R.id.title);
                final EditText position = dialog.findViewById(R.id.position);
                final EditText from = dialog.findViewById(R.id.from);
                final EditText to = dialog.findViewById(R.id.to);
                final ProgressBar bar = dialog.findViewById(R.id.progress);
                Button submit = dialog.findViewById(R.id.submit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String t = title.getText().toString();
                        String p = position.getText().toString();
                        String f = from.getText().toString();
                        String tt = to.getText().toString();

                        if (t.length() > 0)
                        {

                            if (p.length() > 0)
                            {

                                if (f.length() > 0)
                                {

                                    if (tt.length() > 0)
                                    {


                                        bar.setVisibility(View.VISIBLE);

                                        final bean b = (bean) getContext().getApplicationContext();

                                        final Retrofit retrofit = new Retrofit.Builder()
                                                .baseUrl(b.BASE_URL)
                                                .addConverterFactory(ScalarsConverterFactory.create())
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        final AllAPIs cr = retrofit.create(AllAPIs.class);

                                        Call<addCareerBean> call = cr.addCareer(p , t , f , tt , userId);

                                        call.enqueue(new Callback<addCareerBean>() {
                                            @Override
                                            public void onResponse(Call<addCareerBean> call, Response<addCareerBean> response) {

                                                //loadData();

                                                per.loadData();

                                                dialog.dismiss();

                                                bar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onFailure(Call<addCareerBean> call, Throwable t) {
                                                bar.setVisibility(View.GONE);
                                                Log.d("asdasd" , t.toString());
                                            }
                                        });

                                    }
                                    else
                                    {
                                        Toast.makeText(getContext() , "Invalid To Year" , Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(getContext() , "Invalid From Year" , Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(getContext() , "Invalid Position" , Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(getContext() , "Invalid Company Title" , Toast.LENGTH_SHORT).show();
                        }

                    }
                });



            }
        });

        return view;
    }

    public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {
        List<com.yl.youthlive.loginResponsePOJO.Career> list = new ArrayList<>();
        Context context;

        public EducationAdapter(Context context, List<com.yl.youthlive.loginResponsePOJO.Career> list) {
            this.list = list;
            this.context = context;
        }

        public void setGridData(List<com.yl.youthlive.loginResponsePOJO.Career> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.career_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            final com.yl.youthlive.loginResponsePOJO.Career item = list.get(position);

            holder.company.setText(item.getCompany());
            holder.title.setText(item.getPosition());
            holder.time.setText(item.getFrom() + " - " + item.getTo());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.edit_career_popup);
                    dialog.show();

                    final EditText title = (EditText)dialog.findViewById(R.id.title);
                    title.setText(item.getCompany());
                    final EditText position = (EditText)dialog.findViewById(R.id.position);
                    position.setText(item.getPosition());
                    final EditText from = (EditText)dialog.findViewById(R.id.from);
                    from.setText(item.getFrom());
                    final EditText to = (EditText)dialog.findViewById(R.id.to);
                    to.setText(item.getTo());

                    ImageButton delete = (ImageButton)dialog.findViewById(R.id.delete);
                    final ProgressBar progress = (ProgressBar)dialog.findViewById(R.id.progress);
                    Button submit = (Button)dialog.findViewById(R.id.submit);

                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            progress.setVisibility(View.VISIBLE);

                            final bean b = (bean) getContext().getApplicationContext();

                            final Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(b.BASE_URL)
                                    .addConverterFactory(ScalarsConverterFactory.create())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();

                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                            Call<deleteCareerBean> call = cr.deleteCareer(b.userId , item.getCareerId());

                            call.enqueue(new Callback<deleteCareerBean>() {
                                @Override
                                public void onResponse(Call<deleteCareerBean> call, Response<deleteCareerBean> response) {

                                    Toast.makeText(context , response.body().getMessage() , Toast.LENGTH_SHORT).show();

                                    per.loadData();

                                    progress.setVisibility(View.GONE);

                                    dialog.dismiss();

                                }

                                @Override
                                public void onFailure(Call<deleteCareerBean> call, Throwable t) {
                                    progress.setVisibility(View.GONE);
                                }
                            });


                        }
                    });

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            String t = title.getText().toString();
                            String p = position.getText().toString();
                            String f = from.getText().toString();
                            String tt = to.getText().toString();

                            if (t.length() > 0)
                            {

                                if (p.length() > 0)
                                {

                                    if (f.length() > 0)
                                    {

                                        if (tt.length() > 0)
                                        {


                                            progress.setVisibility(View.VISIBLE);

                                            final bean b = (bean) getContext().getApplicationContext();

                                            final Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.BASE_URL)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            final AllAPIs cr = retrofit.create(AllAPIs.class);

                                            Call<editCareerBean> call = cr.editCareer(b.userId , p , t , f , tt , item.getCareerId());

                                            call.enqueue(new Callback<editCareerBean>() {
                                                @Override
                                                public void onResponse(Call<editCareerBean> call, Response<editCareerBean> response) {

                                                    //loadData();

                                                    per.loadData();

                                                    dialog.dismiss();

                                                    progress.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onFailure(Call<editCareerBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                    Log.d("asdasd" , t.toString());
                                                }
                                            });

                                        }
                                        else
                                        {
                                            Toast.makeText(getContext() , "Invalid To Year" , Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    else
                                    {
                                        Toast.makeText(getContext() , "Invalid From Year" , Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else
                                {
                                    Toast.makeText(getContext() , "Invalid Position" , Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(getContext() , "Invalid Company Title" , Toast.LENGTH_SHORT).show();
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

            TextView company , title , time;

            public ViewHolder(View itemView) {
                super(itemView);

                company = itemView.findViewById(R.id.company);
                title = itemView.findViewById(R.id.title);
                time = itemView.findViewById(R.id.time);

            }
        }

    }

}
