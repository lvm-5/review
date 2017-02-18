package com.vlashchevskyi.review.pattern.task;

import com.vlashchevskyi.review.pattern.TestEmulator;

import java.util.concurrent.Callable;

/**
 * Created by lvm on 2/12/17.
 */
public interface ReviewTask<T> extends Callable<T>{
    TestEmulator emulator = new TestEmulator();
    T doAction() throws Exception;
}
