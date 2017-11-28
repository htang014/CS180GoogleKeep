package com.chickendinner.keep.fragments;

import android.app.Fragment;

public class NoteFragment extends Fragment {
    public interface OnFragmentChangeListener {
        public void onFragmentChanged();
    };

    protected OnFragmentChangeListener onChangeListener;

    public NoteFragment() {
        this.onChangeListener = null;
    }

    public void setOnFragmentChangeListener(OnFragmentChangeListener listener) {
        this.onChangeListener = listener;
    }
}
