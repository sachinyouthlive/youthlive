package com.yl.youthlive.checkin;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.youthlive.R;
import com.yl.youthlive.checkinPOJO.Information;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;


public class CheckinAdapter extends RecyclerView.Adapter<CheckinAdapter.MyViewHolder> {

    Context context;
    List<Information> list = new ArrayList<>();


    public CheckinAdapter(Context context, List<Information> list) {
        this.context = context;
        this.list = list;
    }

    public void setGridData(List<Information> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkin_list_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Information item = list.get(position);

        int duration = item.getBroadcastDuration();
        // long millis = 3600000;
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));


        holder.timetxt.setText(hms);
        holder.beantxt.setText(item.getBean().toString());

        String onlytime = item.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse(onlytime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
        String displayValue = timeFormatter.format(date);
        holder.time_title.setText(displayValue);

        int d = item.getDay();

        Log.e("datemonth", item.getMonth().toString());

        switch (item.getMonth()) {
            case 1:
                holder.date.setText(d + ", January");
                break;
            case 2:
                holder.date.setText(d + ", February");
                break;
            case 3:
                holder.date.setText(d + ", March");
                break;
            case 4:
                holder.date.setText(d + ", April");
                break;
            case 5:
                holder.date.setText(d + ", May");
                break;
            case 6:
                holder.date.setText(d + ", June");
                break;
            case 7:
                holder.date.setText(d + ", July");
                break;
            case 8:
                holder.date.setText(d + ", August");
                break;
            case 9:
                holder.date.setText(d + ", September");
                break;
            case 10:
                holder.date.setText(d + ", October");
                break;
            case 11:
                holder.date.setText(d + ", November");
                break;
            case 12:
                holder.date.setText(d + ", December");
                break;
            default:
                holder.date.setText("default");
        }
        //holder.date.setText(item.getDay().toString()+" "+item.getMonth().toString());


        //   DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

        //  ImageLoader loader = ImageLoader.getInstance();
        //  loader.displayImage(item.getTimelineProfileImage() , holder.profile , options);


        //Glide.with(context).load(item.getTimelineProfileImage()).into(holder.profile);
        //Glide.with(context).load(item.getVideoURL()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView beantxt, timetxt, date, time_title;
        ImageView image;
        CircleImageView profile;

        public MyViewHolder(View itemView) {
            super(itemView);
            beantxt = (TextView) itemView.findViewById(R.id.beantxt);
            timetxt = (TextView) itemView.findViewById(R.id.timetxt);
            date = (TextView) itemView.findViewById(R.id.date);
            image = (ImageView) itemView.findViewById(R.id.image);
            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            time_title = itemView.findViewById(R.id.title);
        }
    }

}
