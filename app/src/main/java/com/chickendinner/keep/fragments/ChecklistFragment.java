package com.chickendinner.keep.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chickendinner.keep.R;
import com.chickendinner.keep.beans.CheckListBean;
import com.chickendinner.keep.tools.OnTextChangeListener;
import com.chickendinner.keep.adapters.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChecklistFragment extends NoteFragment {
    private RecyclerView mRecyclerView;
    private RecyclerListAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CheckListBean> mDataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataset = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checklist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle icicle) {
        super.onViewCreated(view, icicle);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecyclerListAdapter(mDataset);
        mRecyclerAdapter.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(int pos, String text) {
                mDataset.get(pos).setText(text);
                onChangeListener.onFragmentChanged();
                Log.e(String.format("%d is ---->",pos),text);
            }

            @Override
            public void onCheckChanged(int pos, boolean check) {
                mDataset.get(pos).setCheck(check);
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    // Functions for external use
    public List<CheckListBean> getDataset() {
        return mDataset;
    }

    public void addItem() {
        mDataset.add(new CheckListBean("", false));
        mRecyclerAdapter.notifyDataSetChanged();
    }

    public void addItem(String text, boolean check) {
        mDataset.add(new CheckListBean(text, check));
        mRecyclerAdapter.notifyDataSetChanged();
    }

}