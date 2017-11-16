package com.chickendinner.keep.Tools;

import android.util.Log;

public class NoteTypeHelper {
    int TextNode = 0;
    int Checklist = 1;
    int Drawing = 2;
    int Picture = 3;

    public void ShowTheNoteType(int type) {
        switch (type) {
            case 0:
                Log.e(String.format("%d is ---->",type),"TextNode"); break;
            case 1:
                Log.e(String.format("%d is ---->",type),"Checklist"); break;
            case 2:
                Log.e(String.format("%d is ---->",type),"Drawing"); break;
            case 3:
                Log.e(String.format("%d is ---->",type),"Picture"); break;
            default:;
        }
    }

}
