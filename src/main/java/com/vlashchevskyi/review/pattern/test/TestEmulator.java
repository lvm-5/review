package com.vlashchevskyi.review.pattern.test;

import java.util.Collection;

/**
 * Created by lvm on 2/18/17.
 */
public class TestEmulator {
    private boolean testMode;
    private int limit;
    private String host;

    public TestEmulator() {
        host = "http://api.google.com:8080";
        limit = 10;
        testMode = false;
    }


    public boolean doToLimit(Collection records) {
        boolean status = true;

        if (testMode) {
            status = !(records.size() >= limit);
        }

        return status;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        if (testMode) {
            return host;
        }

        return host; //TranslateOptions.getDefaultInstance().getHost();
    }

    public void setTestMode(boolean mode) {
        testMode = mode;
    }

    public boolean getTestMode() {
        return testMode;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

}
