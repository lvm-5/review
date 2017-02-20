package com.vlashchevskyi.review.pattern.task;


import com.vlashchevskyi.review.pattern.ReviewSubject;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by lvm on 2/11/17.
 */
public abstract class ReviewTaskObserver<T> implements ReviewTask {

    private List<String[]> records;
    protected ReviewSubject subject;

    @Override
    public T call() throws Exception {
        synchronized (this) {
            do {
                wait();
                doAction();
                subject.updateReadyCounter();
            } while (records.size() > 0 && !emulator.getTestMode());
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
    public Stream<String[]> getRecords() {
        return records.parallelStream();
    }


    protected abstract T getResult();
}
