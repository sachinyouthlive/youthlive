package com.app.youthlive.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.youthlive.R;
import com.app.youthlive.diamondpurchasehistoryPOJO.Information;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class diamondPHAdapter extends RecyclerView.Adapter<diamondPHAdapter.MyViewHolder> {

    Context context;
    List<Information> list = new ArrayList<>();


    public diamondPHAdapter(Context context, List<Information> list) {
        this.context = context;
        this.list = list;
    }

    public void setGridData(List<Information> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diamond_purchasehistory_list_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Information item = list.get(position);


        holder.ordernotxt.setText(item.getOrderId().toString());

        holder.amount.setText(item.getAmount().toString());


        String onlytime = item.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormatter.parse(onlytime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

// Get time from date
        SimpleDateFormat timeFormatter = new SimpleDateFormat("MMM dd yyyy, HH:mm:ss");
        String displayValue = timeFormatter.format(date).toUpperCase();
        holder.date.setText(displayValue);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ordernotxt, date, amount;

        public MyViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.ordercreated);
            ordernotxt = itemView.findViewById(R.id.ordernotxt);
            date = itemView.findViewById(R.id.date);
        }
    }

}
