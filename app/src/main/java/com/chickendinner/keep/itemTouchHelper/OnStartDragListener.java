package com.chickendinner.keep.itemTouchHelper;

import android.support.v7.widget.RecyclerView;

// From iPaulPro Android-ItemTouchHelper-Demo
public interface OnStartDragListener {

    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}