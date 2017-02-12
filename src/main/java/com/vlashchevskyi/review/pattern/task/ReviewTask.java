package com.vlashchevskyi.review.pattern.task;

import java.util.concurrent.Callable;

/**
 * Created by lvm on 2/12/17.
 */
public interface ReviewTask extends Callable{
    Object doAction() throws Exception;
}
