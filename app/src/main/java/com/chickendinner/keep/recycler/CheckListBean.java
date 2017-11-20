package com.chickendinner.keep.recycler;

import java.io.Serializable;

public class CheckListBean implements Serializable {
    String text;
    boolean check;
    public CheckListBean(String text, boolean check) {
        this.text = text;
        this.check = check;
    }
}
