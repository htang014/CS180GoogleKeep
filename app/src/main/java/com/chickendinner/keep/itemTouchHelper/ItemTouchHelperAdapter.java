package com.chickendinner.keep.itemTouchHelper;

// From iPaulPro Android-ItemTouchHelper-Demo
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}