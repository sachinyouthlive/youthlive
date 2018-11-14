package com.app.youthlive;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.app.youthlive.acceptRejectPOJO.acceptRejectBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomGuestSheet extends BottomSheetDialogFragment {

    List<guestBean> guestList;
    ImageView notificationDot;
    String liveId;
    Boolean isConnection;
    TextView reject1;

    public void setData(List<guestBean> guestList , ImageView notificationDot , String liveId , Boolean isConnection , TextView reject1)
    {
        this.guestList = guestList;
        this.notificationDot = notificationDot;
        this.liveId = liveId;
        this.isConnection = isConnection;
        this.reject1 = reject1;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.con_popup , container , false);

        RecyclerView grid = view.findViewById(R.id.grid);
        ProgressBar progress = view.findViewById(R.id.progressBar2);

        GridLayoutManager dialogManager = new GridLayoutManager(getContext(), 1);

        DialogAdapter2 dialogAdapter = new DialogAdapter2(getContext(), guestList, progress , guestList , liveId , isConnection , reject1);

        grid.setAdapter(dialogAdapter);
        grid.setLayoutManager(dialogManager);

        notificationDot.setVisibility(View.GONE);

        return view;
    }

    class DialogAdapter2 extends RecyclerView.Adapter<DialogAdapter2.ViewHolder> {

        Context context;
        List<guestBean> list = new ArrayList<>();
        ProgressBar dialogProgress;
        List<guestBean> guestList;
        String liveId;
        Boolean isConnection;
        TextView reject1;


        public DialogAdapter2(Context context, List<guestBean> list, ProgressBar dialogProgress , List<guestBean> guestList , String liveId , Boolean isConnection , TextView reject1) {
            this.context = context;
            this.list = list;
            this.dialogProgress = dialogProgress;
            this.guestList = guestList;
            this.liveId = liveId;
            this.isConnection = isConnection;
            this.reject1 = reject1;
        }


        public void removeItem(int position)
        {
            try {
                guestList.remove(position);
                notifyItemRemoved(position);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.guest_list_model2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

            final guestBean item = list.get(position);

            final String uid = item.getId().replace("\"", "");
            final String imm = item.getPic().replace("\"", "");
            final String un = item.getName().replace("\"", "");

            final bean b = (bean) context.getApplicationContext();

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage(b.BASE_URL + imm, holder.image, options);

            holder.name.setText(un);

            if (position == list.size() - 1)
            {
                holder.line.setVisibility(View.GONE);
            }
            else
            {
                holder.line.setVisibility(View.VISIBLE);
            }

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialogProgress.setVisibility(View.VISIBLE);

                    final bean b = (bean) context.getApplicationContext();


                    Call<acceptRejectBean> call1 = b.getRetrofit().acceptRejectBroadcaster(item.getConnid(), liveId + uid, "2", uid);
                    call1.enqueue(new Callback<acceptRejectBean>() {
                        @Override
                        public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                            try {


                                isConnection = false;
                                //cameraLayout1.setVisibility(View.VISIBLE);


                                reject1.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            reject1.setVisibility(View.VISIBLE);

                            isConnection = true;



                            dialogProgress.setVisibility(View.GONE);

                            dismiss();

                            removeItem(position);
                        }

                        @Override
                        public void onFailure(Call<acceptRejectBean> call, Throwable t) {
                            dialogProgress.setVisibility(View.GONE);
                            t.printStackTrace();
                        }
                    });


                }
            });


            holder.deny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialogProgress.setVisibility(View.VISIBLE);

                    final bean b = (bean) context.getApplicationContext();


                    Call<acceptRejectBean> call1 = b.getRetrofit().acceptRejectBroadcaster(item.getConnid(), liveId + uid, "1", uid);
                    call1.enqueue(new Callback<acceptRejectBean>() {
                        @Override
                        public void onResponse(Call<acceptRejectBean> call, Response<acceptRejectBean> response) {

                            try {


                                isConnection = false;
                                //cameraLayout1.setVisibility(View.VISIBLE);


                                //reject1.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            dialogProgress.setVisibility(View.GONE);

                            removeItem(position);
                        }

                        @Override
                        public void onFailure(Call<acceptRejectBean> call, Throwable t) {
                            dialogProgress.setVisibility(View.GONE);
                            t.printStackTrace();
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

            CircleImageView image;
            TextView name;
            Button accept, deny;
            TextView line;

            public ViewHolder(View itemView) {
                super(itemView);


                image = itemView.findViewById(R.id.view8);
                name = itemView.findViewById(R.id.textView36);
                accept = itemView.findViewById(R.id.button9);
                deny = itemView.findViewById(R.id.button13);
                line = itemView.findViewById(R.id.textView37);


            }
        }
    }

}
