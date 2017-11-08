package com.chickendinner.keep.recycler;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.chickendinner.keep.R;
import com.chickendinner.keep.itemTouchHelper.ItemTouchHelperAdapter;
import com.chickendinner.keep.itemTouchHelper.ItemTouchHelperCallback;
import com.chickendinner.keep.itemTouchHelper.OnStartDragListener;
import com.chickendinner.keep.recycler.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerListFragment extends Fragment implements
        OnStartDragListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mDataset;
    private ArrayList<CheckBox> mCheckList;


    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper.Callback mItemTouchCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataset = new ArrayList<String>();
        // Inflate the layout for this fragment
        mCheckList = new ArrayList<CheckBox>();
        return inflater.inflate(R.layout.fragment_recycler_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle icicle) {
        super.onViewCreated(view, icicle);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecyclerListAdapter(this, mDataset, mCheckList);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mItemTouchCallback = new ItemTouchHelperCallback((ItemTouchHelperAdapter) mRecyclerAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    // Externally used to push to RecyclerView
    public void addItem() {
        mDataset.add("");
        mRecyclerAdapter.notifyDataSetChanged();
    }

    public List<String> getStringData() {
        return mDataset;
    }

    public List<CheckBox> getCheckList() {
        return mCheckList;
    }



}