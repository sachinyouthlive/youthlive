package com.yl.youthlive.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yl.youthlive.ChatScreen;
import com.yl.youthlive.R;
import com.yl.youthlive.TimeStampConverter;
import com.yl.youthlive.allMessagePOJO.Datum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.MsgViewHolder> {

    private Context context;
    private List<Datum> list;

    public MassageAdapter(Context context, List<Datum> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MsgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (inflater != null) {
            view = inflater.inflate(R.layout.messageformate, parent, false);
        }
        return new MsgViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MsgViewHolder msgViewHolder, int i) {
        msgViewHolder.setIsRecyclable(false);

        final Datum item = list.get(i);

        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(item.getFriendImage(), msgViewHolder.image);

        msgViewHolder.name.setText(item.getFriendName());

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

            // System.out.println("Milliseconds==" + testDate.getTime());
            String mtime = null;
            if (testDate != null) {
                mtime = TimeStampConverter.getTimeAgo(testDate.getTime());
            }
            msgViewHolder.msgtime.setText(mtime);
        }
        //.............

        if (i == list.size() - 1) {
            msgViewHolder.line.setVisibility(View.GONE);
        } else {
            msgViewHolder.line.setVisibility(View.VISIBLE);
        }

        msgViewHolder.lastMsg.setText(item.getLastMsg());

        msgViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ChatScreen.class);
                intent.putExtra("id", item.getFriendId());
                intent.putExtra("chat", item.getChatId());
                intent.putExtra("name", item.getFriendName());
                intent.putExtra("image", item.getFriendImage());
                // Toast.makeText(context, "" + item.getFriendId() + " " + item.getChatId() + " " + item.getFriendName(), Toast.LENGTH_SHORT).show();
                context.startActivity(intent);

            }
        });

    }

    public void setgrid(List<Datum> list) {

        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MsgViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView name, msgtime;
        TextView line , lastMsg;

        MsgViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            msgtime = itemView.findViewById(R.id.msg_time);
            line = itemView.findViewById(R.id.line);
            lastMsg = itemView.findViewById(R.id.textView59);

        }
    }
}
