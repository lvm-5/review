package com.vlashchevskyi.tool.test;

import com.vlashchevskyi.tool.memory.MemoryCalc;

import java.util.Collection;

/**
 * Created by lvm on 2/18/17.
 */
public class TestEmulator {
    private boolean testMode;
    private int limit;
    private long memoryLimit = 500;
    private String host;

    public boolean isLimit(Collection records) {
        if (testMode) {
            return records.size() >= limit;
        } else {
            return false;
        }
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

    public long getMemoryLimit(){
        if (testMode) {
            return memoryLimit;
        }
        else {
            return MemoryCalc.MEMORY_LIMIT;
        }
    }

    public void setMemoryLimit(long inMb) {
        memoryLimit = inMb;
    }

    public TestEmulator() {
        host = "http://api.google.com:8080";
        limit = 10;
        testMode = false;
    }
}
