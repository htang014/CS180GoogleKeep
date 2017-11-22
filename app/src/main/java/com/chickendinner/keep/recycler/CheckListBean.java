package com.chickendinner.keep.recycler;

import java.io.Serializable;

public class CheckListBean implements Serializable {
    String text;
    boolean check;

    public boolean isCheck() {
        return check;
    }
    public void setCheck(boolean check) {
        this.check = check;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public CheckListBean(String text, boolean check) {
        this.text = text;
        this.check = check;
    }
}
