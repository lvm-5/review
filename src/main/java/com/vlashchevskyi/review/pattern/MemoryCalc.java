package com.vlashchevskyi.review.pattern;

/**
 * Created by lvm on 2/15/17.
 */
public class MemoryCalc {
    private long limit;
    private float threshold;
    private Runtime rt = Runtime.getRuntime();

    private final float COEFF = 2f;
    private final int BYTE_TO_MB = 1024 * 1024;

    public boolean isFreeMemory() throws InterruptedException {
        long delta = limit - rt.totalMemory();
        return calcUsedMemory() < getThreshold() + delta / 16;
    }

    public float calcUsedMemory() {
        return rt.totalMemory() - rt.freeMemory();
    }

    public void freeMem() {
        rt.gc();
    }

    public long toMB(float mem) {
        return new Float(mem / BYTE_TO_MB).longValue();
    }

    public void setLimit(long inMb) {
        limit = inMb * BYTE_TO_MB;
    }

    public long getLImit() {
        return new Float(limit / BYTE_TO_MB).intValue();
    }

    public float getThreshold() {
        return threshold;
    }

    public MemoryCalc(long limitInMb) {
        setLimit(limitInMb);
        threshold = limit / COEFF;
    }
}
