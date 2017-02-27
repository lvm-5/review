package com.vlashchevskyi.tool.memory;

import com.vlashchevskyi.tool.Delay;
import com.vlashchevskyi.tool.test.Emulator;
import org.apache.log4j.Logger;

/**
 * Created by lvm on 2/27/17.
 */
public class MemoryMonitorTask implements Runnable, Emulator {
    private MemoryCalc mCalc = new MemoryCalc(MemoryCalc.MEMORY_LIMIT);
    protected final Logger logger = Logger.getLogger("com.vlashchevskyi.review.pattern");

    @Override
    public void run() {
        Delay dl = new Delay(Delay.DELAY);
        int safety = 100;

        while (true) {
            if (!dl.doPauseIf(mCalc.isFreeMemory(safety))) {
                mCalc.gc();
                dl.doWait(Delay.DELAY);

                if (!mCalc.isFreeMemory()) {
                    logger.warn("\n Memory usage: "
                            + "\n - total memory = "
                            + mCalc.toMB(mCalc.getRt().totalMemory()) + "Mb"
                            + "\n - free memory = "
                            + mCalc.toMB(mCalc.getRt().freeMemory()) + "Mb"
                            + "\n memory is limited to "
                            + MemoryCalc.MEMORY_LIMIT + "Mb! \n");
                }
            }
        }
    }
}
