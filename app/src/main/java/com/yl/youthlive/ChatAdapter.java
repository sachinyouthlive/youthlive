package com.yl.youthlive;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yl.youthlive.singleMessagePOJO.Datum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by TBX on 11/24/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<Datum> list = new ArrayList<>();
    Context context;


    public ChatAdapter(Context context, List<Datum> list) {
        this.list = list;
        this.context = context;
    }

    public void setGridData(List<Datum> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chat_list_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Datum item = list.get(position);

        holder.message.setText(item.getLastMsg());

        // setting msg time
        String dateString = item.getLastMsgTime();
        if (dateString != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date testDate = null;
            try {
                testDate = sdf.parse(dateString);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.e("time stamp", dateString);
            Log.e("time millis", String.valueOf(testDate.getTime()));
            String mtime = TimeStampConverter.getTimeAgo(testDate.getTime());
            holder.time.setText(mtime);
        }
        //.............

        bean b = (bean) context.getApplicationContext();

        if (Objects.equals(item.getSenderId(), SharePreferenceUtils.getInstance().getString("userId"))) {

            holder.container.setGravity(Gravity.END);
            holder.bubble.setBackgroundResource(R.drawable.bubble_me);

        } else {
            holder.container.setGravity(Gravity.START);
            holder.bubble.setBackgroundResource(R.drawable.bubble);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout container, bubble;
        TextView message, time;

        public ViewHolder(View itemView) {
            super(itemView);

            container = (LinearLayout) itemView.findViewById(R.id.container);
            bubble = (LinearLayout) itemView.findViewById(R.id.bubble);
            message = (TextView) itemView.findViewById(R.id.message);
            time = (TextView) itemView.findViewById(R.id.time);

        }
    }

}
