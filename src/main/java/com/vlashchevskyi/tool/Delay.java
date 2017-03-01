package com.vlashchevskyi.tool;

/**
 * Created by lvm on 2/22/17.
 */
public class Delay {
    private static int TIME_LIMIT = 10000;
    public static final int DELAY = 1000;

    private long delay = 1;

    public synchronized void doWait(int time) {
        try {
            wait(time);
        } catch (InterruptedException e) {
            // logger place
        }
    }
    public synchronized boolean doPauseIf(Boolean condition) {
        if (!condition) return false;

        try {
            delay = (delay < TIME_LIMIT)? ++delay: TIME_LIMIT;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
           // logger place
        }

        return true;
    }

    public Delay() {

    }

    public Delay(int delay) {
        this.delay = delay;
    }
}
