package com.chickendinner.keep.Tools;

import java.util.Random;



public class NoteIdGenerator {

    private final int IdLength = 20;

    //note id is 10 bit string, include the upper lower case character and numbers.
    public String generateNoteId () {
        /*
        0 - 9 number
        10 - 35 A - Z
        36 - 62 a - z
         */
        int max = 10 + 26 * 2;
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < IdLength; i ++) {
            int tp = random.nextInt(max);
            if (tp < 10) {
                sb.append(tp);
            } else if (tp < 36) {
                sb.append((char)('A' + (tp - 10)));
            } else {
                sb.append((char)('a' + (tp - 36)));
            }
        }
        return sb.toString();
    }

}
