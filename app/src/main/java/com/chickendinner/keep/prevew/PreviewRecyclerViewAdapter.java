package com.chickendinner.keep.prevew;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chickendinner.keep.R;

import java.util.List;


public class PreviewRecyclerViewAdapter extends RecyclerView.Adapter<PreviewRecyclerViewAdapter.ViewHolder> {
    private List<PreviewListBean> mDataset;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_preview_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PreviewRecyclerViewAdapter.ViewHolder vh = new PreviewRecyclerViewAdapter.ViewHolder(l);
        vh.setIsRecyclable(false);
        return vh;}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position).title);
    }

    public PreviewRecyclerViewAdapter(List<PreviewListBean> initData) {
        this.mDataset = initData;
    }

    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mPreviewItem;
        public TextView mTextView;
        public ViewHolder(LinearLayout view) {
            super(view);
            mPreviewItem = view;
            mTextView = (TextView) itemView.findViewById(R.id.preview_item);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
        }
    }

}
