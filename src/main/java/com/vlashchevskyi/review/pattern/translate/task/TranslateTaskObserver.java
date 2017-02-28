package com.vlashchevskyi.review.pattern.translate.task;

import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;

import java.util.List;

/**
 * Created by lvm on 2/28/17.
 */
public abstract class TranslateTaskObserver<T, U extends List> extends ReviewTaskObserver<T, U> {

    @Override
    protected boolean isExit() {
        return true;
    }
}
