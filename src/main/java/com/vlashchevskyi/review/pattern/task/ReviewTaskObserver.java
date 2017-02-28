package com.vlashchevskyi.review.pattern.task;


import com.vlashchevskyi.review.pattern.ReviewSubject;

import java.util.List;

/**
 * Created by lvm on 2/11/17.
 */
public abstract class ReviewTaskObserver<T, U extends List> implements ReviewTask<T> {

    private U resource;
    protected ReviewSubject<U> subject;

    @Override
    public T call() throws Exception {
        synchronized (this) {
            do {
                subject.updateReadyCounter();
                wait();
                try {
                    doAction();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            } while (!isExit() && !emulator.getTestMode());
        }
        return getResult();
    }

    protected boolean isExit() {
        return resource.size() == 0;
    }

    public void setSubject(ReviewSubject subject) {
        this.subject = subject;
    }

    public void handle() {
        U recs = subject.getResource();
        if (!recs.equals(resource)) {
            synchronized (this) {
                resource = recs;
                notify();
            }
        }
    }
    public U getResource() {
        return resource;
    }

    protected abstract T getResult();

    public ReviewTaskObserver() {
    }
}
