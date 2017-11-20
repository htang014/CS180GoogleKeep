package com.chickendinner.keep.recycler;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chickendinner.keep.R;
import com.chickendinner.keep.recycler.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerListFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerListAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CheckListBean> mDataset;

    public List<CheckListBean> getmDataset() {
        return mDataset;
    }

    public void setmDataset(List<CheckListBean> mDataset) {
        this.mDataset = mDataset;
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataset = new ArrayList<>();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle icicle) {
        super.onViewCreated(view, icicle);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerAdapter = new RecyclerListAdapter(mDataset);
        mRecyclerAdapter.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(int pos, String text) {
                mDataset.get(pos).text = text;
                Log.e(String.format("%d is ---->",pos),text);
            }

            @Override
            public void onCheckChanged(int pos, boolean check) {
                mDataset.get(pos).check = check;
            }
        });
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    // Externally used to push to RecyclerView
    public void addItem() {
        mDataset.add(new CheckListBean("", false));
        mRecyclerAdapter.notifyDataSetChanged();
    }

    public void addItem(String text, boolean check) {
        mDataset.add(new CheckListBean(text, check));
        mRecyclerAdapter.notifyDataSetChanged();
    }

}