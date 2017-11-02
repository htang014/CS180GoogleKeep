package com.chickendinner.keep.recycler;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chickendinner.keep.R;
import com.chickendinner.keep.itemTouchHelper.ItemTouchHelperAdapter;
import com.chickendinner.keep.itemTouchHelper.OnStartDragListener;

import java.util.Collections;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {
    private List<String> mDataset;
    private final OnStartDragListener mDragStartListener;

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
    public RecyclerListAdapter(OnStartDragListener dragStartListener, List<String> initData) {
        mDragStartListener = dragStartListener;
        mDataset = initData;
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
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerListAdapter.ViewHolder holder, int position) {
        // Initialize EditText contents to string added to dataset.  Does nothing currently.
        EditText t = (EditText) holder.mChecklistItem.getChildAt(2);
        t.setText(mDataset.get(mDataset.size()-1));

        // From iPaulPro Android-ItemTouchHelper-Demo
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    // From iPaulPro Android-ItemTouchHelper-Demo
    // Implements drag and drop rearrangement for recycler
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mDataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mDataset, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    // From iPaulPro Android-ItemTouchHelper-Demo
    // Implements swipe deletion for recycler
    @Override
    public void onItemDismiss(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}