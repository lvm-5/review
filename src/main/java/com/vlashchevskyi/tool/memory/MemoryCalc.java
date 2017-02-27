package com.vlashchevskyi.tool.memory;

import org.apache.log4j.Logger;

/**
 * Created by lvm on 2/15/17.
 */
public class MemoryCalc {
    public static final int MEMORY_LIMIT = 500;

    private long limit;
    private final Runtime rt;
    private final MemoryUsage mUse;

    private final int MB_IN_BYTE = 1024 * 1024;

    protected final Logger logger = Logger.getLogger("com.vlashchevskyi.review.pattern");


    public long calcCapacity() throws InterruptedException {
        if (mUse.getTotal() == null) {
            mUse.init();
        }

        return mUse.calcCapacity(rt.totalMemory());
    }

    public boolean isFreeMemory() {
        return rt.freeMemory() > 0 && rt.totalMemory() <= limit ;
    }

    public boolean isFreeMemory(long safety) {
        return rt.freeMemory() > 0 && rt.totalMemory() <= limit - safety * MB_IN_BYTE ;
    }

    public void gc() {
        rt.gc();
    }

    public long toMB(float mem) {
        return new Float(mem / MB_IN_BYTE).longValue();
    }


    public void setLimit(long inMb) {
        limit = inMb * MB_IN_BYTE;
    }

    public long getLImit() {
        return new Float(limit / MB_IN_BYTE).intValue();
    }


    public Runtime getRt() {
        return rt;
    }

    private class MemoryUsage {
        private Long total;
        private Long records;
        private Float rate;

        long calcCapacity(long mem) {
            long records = 0;

            if (isFreeMemory()) {
                initRate(mem);

                if (rate == null) {
                    records = this.records * 2;
                } else {
                    records = Math.round((limit - mem) * rate);
                }
            }

            update(mem, records);
            if (records == 0) {
                logger.warn("Capacity is zero. Some records arent' read!");
            }
            return records;
        }

        private float calcRate(long mem) {
            float rt = 0f;

            long change = Math.abs(mem - total);
            if (change > 0) {
                rt = 1f * this.records / change;
            }

            return rt;
        }

        private void update(long mem, long records) {
            this.total = mem;
            this.records = records;
        }

        void init() {
            total = rt.totalMemory();
            records = 100l;
        }

        private void initRate(long mem) {
            if (rate == null) {
                float rt = calcRate(mem);
                rate = (rt > 0)? rt: null;
            }
        }

        public Long getTotal() {
            return total;
        }
    }

    public MemoryCalc(long limit) {
        this();
        setLimit(limit);

    }

    public MemoryCalc() {
        limit = MEMORY_LIMIT;
        mUse = new MemoryUsage();
        rt = Runtime.getRuntime();
    }
}


