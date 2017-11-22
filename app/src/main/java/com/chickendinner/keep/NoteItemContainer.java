package com.chickendinner.keep;

import com.chickendinner.keep.recycler.CheckListBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hanst on 11/21/2017.
 */

public class NoteItemContainer {
    private String mTitle;
    //private String mData;
    //private Map<String, Object> mListData;
    private String mType;

    public NoteItemContainer() {
        //Default constructor
    }

    public NoteItemContainer(String title, String type){
        //mData = data;
        //mListData = listData;
        mTitle = title;
        mType = type;
    }

    //public String getData() { return mData; }

    //public void setData(String data) { mData = data; }

    /*public Map<String, Object> getListData() { return mListData; }

    public void setListData(Map<String, Object> listData) { mListData = listData; }*/

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }
}