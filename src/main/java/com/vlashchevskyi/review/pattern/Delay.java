package com.vlashchevskyi.review.pattern;

/**
 * Created by lvm on 2/22/17.
 */
public class Delay {
    private static int TIME_LIMIT = 5000;
    private long delay = 1;

    public synchronized boolean doPauseIf(Boolean condition) {
        if (!condition) return false;

        try {
            delay = (delay < TIME_LIMIT)? ++delay: TIME_LIMIT;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
}
