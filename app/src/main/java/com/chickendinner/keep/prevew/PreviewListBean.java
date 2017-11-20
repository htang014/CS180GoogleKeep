package com.chickendinner.keep.prevew;

public class PreviewListBean {
    String noteId;
    String title;
    String type;
    Object data;

    public PreviewListBean(String noteId, String title, String type, Object data) {
        this.noteId = noteId;
        this.title = title;
        this.type = type;
        this.data = data;
    }

}
