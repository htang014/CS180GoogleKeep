package com.chickendinner.keep.recycler;

import java.io.Serializable;

public class CheckListBean implements Serializable {
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

    String text;
    boolean check;
    public CheckListBean(String text, boolean check) {
        this.text = text;
        this.check = check;
    }
}
