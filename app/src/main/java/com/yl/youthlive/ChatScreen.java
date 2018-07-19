package com.yl.youthlive;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.sendMessagePOJO.sendMessageBean;
import com.yl.youthlive.singleMessagePOJO.Datum;
import com.yl.youthlive.singleMessagePOJO.singleMessageBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChatScreen extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView grid;
    LinearLayoutManager manager;
    String id, name, image, chat;
    ChatAdapter adapter;
    List<Datum> list;
    ProgressBar progress;
    EditText comment;
    FloatingActionButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);

        list = new ArrayList<>();

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        image = getIntent().getStringExtra("image");
        chat = getIntent().getStringExtra("chat");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        grid = (RecyclerView) findViewById(R.id.grid);
        progress = (ProgressBar) findViewById(R.id.progress);
        comment = (EditText) findViewById(R.id.comment);
        send = (FloatingActionButton) findViewById(R.id.send);

        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.arrow);

        toolbar.setTitle(name);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        adapter = new ChatAdapter(this, list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String comm = comment.getText().toString();

                if (comm.length() > 0) {

                    progress.setVisibility(View.VISIBLE);

                    final bean b = (bean) getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);
                    Call<sendMessageBean> call = cr.sendMessage(b.userId, id, comm);

                    call.enqueue(new Callback<sendMessageBean>() {
                        @Override
                        public void onResponse(Call<sendMessageBean> call, Response<sendMessageBean> response) {

                            comment.setText("");
                            progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<sendMessageBean> call, Throwable t) {

                            progress.setVisibility(View.GONE);

                        }
                    });

                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData() {

        progress.setVisibility(View.VISIBLE);

        bean b = (bean) getApplicationContext();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);
        //  Toast.makeText(b, "" + chat + " frnd id" + id + " usrid" + b.userId, Toast.LENGTH_SHORT).show();
        Call<singleMessageBean> call = cr.singleChatList(b.userId, id, chat);

        call.enqueue(new Callback<singleMessageBean>() {
            @Override
            public void onResponse(Call<singleMessageBean> call, Response<singleMessageBean> response) {

                adapter.setGridData(response.body().getData());
                progress.setVisibility(View.GONE);

                //     Toast.makeText(ChatScreen.this, "loaddata", Toast.LENGTH_SHORT).show();
                schedule();

            }

            @Override
            public void onFailure(Call<singleMessageBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }


    public void schedule() {

        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                final bean b = (bean) getApplicationContext();

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);


                Call<singleMessageBean> call = cr.singleChatList(b.userId, id, chat);

                call.enqueue(new Callback<singleMessageBean>() {
                    @Override
                    public void onResponse(Call<singleMessageBean> call, Response<singleMessageBean> response) {


                        try {
                            if (response.body().getData() != null) {
                                adapter.setGridData(response.body().getData());
                                grid.scrollBy(0, 200);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<singleMessageBean> call, Throwable t) {

                    }
                });

            }
        }, 0, 1000);

    }


}

