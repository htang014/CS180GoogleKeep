package com.chickendinner.keep.prevew;


import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chickendinner.keep.R;
import com.chickendinner.keep.recycler.CheckListBean;

import java.util.List;


public class PreviewRecyclerViewAdapter extends RecyclerView.Adapter<PreviewRecyclerViewAdapter.ViewHolder> {
    private List<CheckListBean> mDataset;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mChecklistItem;
        public ImageView handleView;
        public ViewHolder(LinearLayout l) {
            super(l);
            mChecklistItem = l;
            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }
    }

}
