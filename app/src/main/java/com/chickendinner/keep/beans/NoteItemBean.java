package com.chickendinner.keep.beans;

public class NoteItemBean {
    private String mTitle;
    private String mType;

    public NoteItemBean() {
        //Default constructor
    }

    public NoteItemBean(String title, String type){
        mTitle = title;
        mType = type;
    }

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