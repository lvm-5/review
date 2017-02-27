package com.vlashchevskyi.review.pattern.task;

import com.vlashchevskyi.review.pattern.MemoryCalc;
import com.vlashchevskyi.review.pattern.test.Emulator;

import java.util.concurrent.Callable;

/**
 * Created by lvm on 2/12/17.
 */
public interface ReviewTask<T> extends Callable<T>, Emulator{
    MemoryCalc mCalc = new MemoryCalc(emulator.getMemoryLimit());
    T doAction() throws Exception;
}
