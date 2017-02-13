package com.vlashchevskyi.review.pattern.task;


import com.vlashchevskyi.review.pattern.ReviewSubject;

import java.util.List;

/**
 * Created by lvm on 2/11/17.
 */
public abstract class ReviewTaskObserver<T> implements ReviewTask {

    private List<String[]> records;
    protected ReviewSubject subject;

    private boolean testMode = false;

    @Override
    public T call() throws Exception {
        synchronized (this) {
            do {
                wait();
                doAction();
                subject.updateReadyCounter();
            } while (records.size() > 0 && !testMode);
        }
        return getResult();
    }

    public void setSubject(ReviewSubject subject) {
        this.subject = subject;
    }

    public void handle() {
        List<String[]> recs = subject.getRecords();
        if (!recs.equals(records)) {
            synchronized (this) {
                records = recs;
                notify();
            }
        }
    }
    public List<String[]> getRecords() {
        return records;
    }

    public void setTestMode(boolean mode) {
        testMode = mode;
    }
    public boolean getTestMode() {
        return testMode;
    }

    protected abstract T getResult();
}
