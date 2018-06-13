package com.yl.youthlive.checkin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yl.youthlive.R;

import java.util.Calendar;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private int[] mData = new int[0];
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private int mMonth;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, int[] data, int month) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mMonth = month;

    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Calendar c1 = Calendar.getInstance();
        int iDay = c1.get(Calendar.DATE);
        int iMonth = c1.get(Calendar.MONTH);

        String dayNumber = String.valueOf(mData[position]);

        if (mMonth < iMonth) {
            holder.shadeImg.setBackgroundResource(R.drawable.dark_shade);
            holder.myTextView.setText(dayNumber);
        } else if (mMonth == iMonth) {
            if ((Integer.parseInt(dayNumber)) < iDay) {
                holder.shadeImg.setBackgroundResource(R.drawable.dark_shade);
            }
            if (dayNumber == String.valueOf(iDay)) {

                holder.myTextView.setText("Today");
                holder.myTextView.setBackgroundColor(Color.parseColor("#FFCA18"));
            } else {
                holder.myTextView.setText(dayNumber);
            }
        } else {
            holder.myTextView.setText(dayNumber);
        }

        holder.myImageView.setImageResource(R.drawable.coin);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }

    ////

    // convenience method for getting data at click position
    int getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView, shadeImg;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            myImageView = itemView.findViewById(R.id.info_img);
            shadeImg = itemView.findViewById(R.id.shade_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

            Intent i = new Intent(view.getContext(), CheckInDetailActivity.class);
            // Pass image index
            Bundle bundle = new Bundle();
            bundle.putString("position", String.valueOf(getAdapterPosition() + 1));
            bundle.putInt("month", mMonth);
            i.putExtras(bundle);
            view.getContext().startActivity(i);
        }
    }
}
