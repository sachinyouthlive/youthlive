package com.yl.youthlive;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yl.youthlive.INTERFACE.AllAPIs;
import com.yl.youthlive.sendGiftPOJO.sendGiftBean;

import java.util.Objects;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class BottomGiftSheet extends BottomSheetDialogFragment {


    String liveId, timelineId;


    public void setData(String liveId, String timelineId) {
        this.liveId = liveId;
        this.timelineId = timelineId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_popup, container, false);

        RecyclerView giftGrid = view.findViewById(R.id.grid);
        ProgressBar bar = view.findViewById(R.id.progressBar8);

        GridLayoutManager giftManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);

        GiftAdapter giftAdapter = new GiftAdapter(getActivity(), bar, liveId, timelineId);

        giftGrid.setLayoutManager(giftManager);
        giftGrid.setAdapter(giftAdapter);


        return view;
    }

    class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

        Context context;
        ProgressBar progressBar;
        String liveId, timelineId;

        public GiftAdapter(Context context, ProgressBar progress, String liveId, String timelineId) {
            this.context = context;
            this.progressBar = progress;
            this.liveId = liveId;
            this.timelineId = timelineId;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.gift_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            bean b = (bean) context.getApplicationContext();

            Glide.with(context).load(b.gifts[position]).into(holder.image);

            holder.send.setText(b.diamonds[position]);

            holder.name.setText(b.names[position]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.itemView.setEnabled(false);
                    holder.itemView.setClickable(false);


                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.send_gift_popup);
                    dialog.show();


                    ImageView gif = dialog.findViewById(R.id.imageView23);
                    Button yes = dialog.findViewById(R.id.button14);
                    Button no = dialog.findViewById(R.id.button15);


                    Glide.with(context).load(R.drawable.tenor).into(gif);


                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            progressBar.setVisibility(View.VISIBLE);

                            final bean b = (bean) getContext().getApplicationContext();

                            retrofit2.Call<sendGiftBean> call = b.getRetrofit().sendGift(SharePreferenceUtils.getInstance().getString("userId"), liveId, timelineId, String.valueOf(position + 1), "1", b.diamonds[position]);


                            call.enqueue(new retrofit2.Callback<sendGiftBean>() {
                                @Override
                                public void onResponse(retrofit2.Call<sendGiftBean> call, retrofit2.Response<sendGiftBean> response) {

                                    //Log.d("Asdasdsa", response.body().getMessage());


                                    try {
                                        if (Objects.equals(response.body().getStatus(), "1")) {
                                            //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                                        e.printStackTrace();
                                    }

                                    holder.itemView.setEnabled(true);
                                    holder.itemView.setClickable(true);
                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onFailure(retrofit2.Call<sendGiftBean> call, Throwable t) {
                                    Log.d("asdasd", t.toString());
                                    progressBar.setVisibility(View.GONE);
                                    holder.itemView.setEnabled(true);
                                    holder.itemView.setClickable(true);
                                }
                            });

                        }
                    });

                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            holder.itemView.setEnabled(true);
                            holder.itemView.setClickable(true);
                        }
                    });

                    /*progressBar.setVisibility(View.VISIBLE);

                    final bean b = (bean) getContext().getApplicationContext();

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.BASE_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);


                    retrofit2.Call<sendGiftBean> call = cr.sendGift(SharePreferenceUtils.getInstance().getString("userId"), liveId, timelineId, String.valueOf(position + 1), "1", b.diamonds[position]);


                    call.enqueue(new retrofit2.Callback<sendGiftBean>() {
                        @Override
                        public void onResponse(retrofit2.Call<sendGiftBean> call, retrofit2.Response<sendGiftBean> response) {

                            //Log.d("Asdasdsa", response.body().getMessage());


                            try {
                                if (Objects.equals(response.body().getStatus(), "1")) {
                                    //Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();



                                } else {
                                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Some Error Occurred, Please try again", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }

                            holder.send.setEnabled(true);
                            holder.send.setClickable(true);
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<sendGiftBean> call, Throwable t) {
                            Log.d("asdasd", t.toString());
                            progressBar.setVisibility(View.GONE);
                            holder.send.setEnabled(true);
                            holder.send.setClickable(true);
                        }
                    });*/


                }
            });


        }

        @Override
        public int getItemCount() {
            bean b = (bean) context.getApplicationContext();
            return b.diamonds.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView image;
            Button send;
            TextView name;

            public ViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView12);
                send = itemView.findViewById(R.id.button8);
                name = itemView.findViewById(R.id.textView48);

            }
        }
    }

}
