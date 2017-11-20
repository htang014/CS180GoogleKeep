package com.chickendinner.keep.prevew;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chickendinner.keep.ChecklistActivity;
import com.chickendinner.keep.DrawingActivity;
import com.chickendinner.keep.PhotoNoteActivity;
import com.chickendinner.keep.R;
import com.chickendinner.keep.TextNoteActivity;
import com.chickendinner.keep.recycler.CheckListBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PreviewRecyclerViewAdapter extends RecyclerView.Adapter<PreviewRecyclerViewAdapter.ViewHolder> {
    private List<PreviewListBean> mDataset;
    private final Context mContext;

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

    public PreviewRecyclerViewAdapter(List<PreviewListBean> initData, Context mContext) {
        this.mDataset = initData;
        this.mContext = mContext;
    }

    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mPreviewItem;
        public TextView mTextView;
        public ViewHolder(LinearLayout view) {
            super(view);
            mPreviewItem = view;
            mTextView = (TextView) itemView.findViewById(R.id.preview_item);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.e("NormalTextViewHolder", "onClick--> position = " + getPosition());
                    PreviewListBean pb = mDataset.get(getPosition());
                    Intent intent = null;
                    if (pb.type.equals("0")) {
                        intent = new Intent(mContext, TextNoteActivity.class);
                        intent.putExtra("title", pb.title);
                        intent.putExtra("data", (String) pb.data);
                    } else if (pb.type.equals("1")) {
                        intent = new Intent(mContext, ChecklistActivity.class);
                        intent.putExtra("title", pb.title);
                        intent.putExtra("data", (Serializable)(List<CheckListBean>) pb.data);
                    } else if (pb.type.equals("2")) {
                        intent = new Intent(mContext, DrawingActivity.class);
                        intent.putExtra("title", pb.title);
                        intent.putExtra("data", (String) pb.data);
                    } else {
                        intent = new Intent(mContext, PhotoNoteActivity.class);
                        intent.putExtra("title", pb.title);
                        intent.putExtra("data", (String) pb.data);
                    }
                    intent.putExtra("noteId", pb.noteId);
                    mContext.startActivity(intent);
                }
            });
        }
    }

}
