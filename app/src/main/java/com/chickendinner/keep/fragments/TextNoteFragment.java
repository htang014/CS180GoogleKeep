package com.chickendinner.keep.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.chickendinner.keep.R;

public class TextNoteFragment extends NoteFragment {
    private EditText mTextNoteBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_text_note, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle icicle) {
        super.onViewCreated(view, icicle);
        mTextNoteBody = (EditText) view.findViewById(R.id.textNoteBody);
        mTextNoteBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onChangeListener.onFragmentChanged();
            }
        });
    }

    // Functions for external use
    public String getText() {
        return mTextNoteBody.getText().toString();
    }

    public void setText(String text){
        mTextNoteBody.setText(text);
    }
}