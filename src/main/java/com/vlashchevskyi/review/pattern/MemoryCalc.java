package com.vlashchevskyi.review.pattern;

import org.apache.log4j.Logger;

import static com.vlashchevskyi.review.pattern.ReviewConstants.MEMORY_LIMIT;

/**
 * Created by lvm on 2/15/17.
 */
public class MemoryCalc {
    private long limit;
    private final Runtime rt;
    private final MemoryUsage mUse;

    private final int BYTE_TO_MB = 1024 * 1024;

    protected final Logger logger = Logger.getLogger("com.vlashchevskyi.review.pattern");


    public long calcCapacity() throws InterruptedException {
        if (mUse.getTotal() == null) {
            mUse.init();
        }

        return mUse.calcCapacity(rt.totalMemory());
    }

    public boolean isFreeMemory() {
        return rt.freeMemory() > 0 && rt.totalMemory() <= limit - limit / 10;
    }

    public void gc() {
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


    public Runtime getRt() {
        return rt;
    }

    private class MemoryUsage {
        private Long total;
        private Long records;
        private Float rate;

        private void initRate(long mem) {
            float rt = calcRate(mem);
            if (rt > 0 && rate == null) {
                rate = rt;
            }
        }

        long calcCapacity(long mem) {
            long records = 0;

            if (!isFreeMemory()) {
                gc();
            }

            if (isFreeMemory()) {
                initRate(mem);

                if (rate == null) {
                    records = this.records * 2;
                } else {
                    records = Math.round((limit - mem) * rate);
                }
            } else {
              logger.warn("\n Memory usage: "
                      + "\n - total memory = "
                      + toMB(rt.totalMemory()) + "Mb"
                      + "\n - free memory = "
                      + toMB(rt.freeMemory())+ "Mb"
                      + "\n memory is limited to "
                      + MEMORY_LIMIT + "Mb! \n");
            }

            update(mem, records);

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

        public Long getTotal() {
            return total;
        }
    }

    public MemoryCalc(long limit) {
        setLimit(limit);
        mUse = new MemoryUsage();
        rt = Runtime.getRuntime();
    }
}


