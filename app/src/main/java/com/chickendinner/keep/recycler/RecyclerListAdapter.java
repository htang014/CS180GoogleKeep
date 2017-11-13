package com.chickendinner.keep.recycler;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chickendinner.keep.R;

import java.util.Collections;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {
    private List<CheckListBean> mDataset;
    private OnTextChangeListener mTextListener;

    // Provide a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mChecklistItem;
        public ImageView handleView;
        public ViewHolder(LinearLayout l) {
            super(l);
            mChecklistItem = l;
            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }
    }

    // Constructor
    public RecyclerListAdapter(List<CheckListBean> initData) {
        mDataset = initData;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener){
        this.mTextListener=onTextChangeListener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        LinearLayout l = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_checklist_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        RecyclerListAdapter.ViewHolder vh = new RecyclerListAdapter.ViewHolder(l);
        vh.setIsRecyclable(false);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerListAdapter.ViewHolder holder, final int position) {
        // Initialize EditText contents to string added to dataset.  Does nothing currently.

        EditText t = (EditText) holder.mChecklistItem.getChildAt(2);
        t.setText(mDataset.get(position).text);
        t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTextListener.onTextChanged(position, ((EditText) holder.mChecklistItem.getChildAt(2)).getText().toString());
            }
        });
        CheckBox cb = (CheckBox) holder.mChecklistItem.getChildAt(1);
        cb.setOnCheckedChangeListener(null);
        cb.setChecked(mDataset.get(position).check);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mTextListener.onCheckChanged(position, ((CheckBox) holder.mChecklistItem.getChildAt(1)).isChecked());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}