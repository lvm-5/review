package com.vlashchevskyi.review.pattern;

import java.util.Collection;

/**
 * Created by lvm on 2/18/17.
 */
public class TestEmulator {
    private boolean testMode = false;
    private int limit = 1000;

    public int getLimit() {
        return limit;
    }

    public void setTestMode(boolean mode) {
        testMode = mode;
    }

    public boolean getTestMode() {
        return testMode;
    }

    public boolean doToLimit(Collection records) {
        boolean status = true;

        if (testMode) {
            status = !(records.size() >= limit);
        }

        return status;
    }

}
