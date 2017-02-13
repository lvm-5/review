package com.vlashchevskyi.review.pattern;


import com.vlashchevskyi.review.pattern.task.ReviewTask;

import java.util.List;

/**
 * Created by lvm on 2/11/17.
 */
public abstract class ReviewTaskObserver<T> implements ReviewTask {

    protected List<String[]> records;
    protected ReviewSubject subject;

    public ReviewTaskObserver() {
    }

    @Override
    public T call() throws Exception {
        synchronized (this) {
            do {
                wait();
                try {
                    doAction();
                }catch (Exception e) {
                    e.printStackTrace();
                }
                //System.out.print("Thread B: " + Thread.currentThread().getId());
                subject.updateReadyCounter();
                //System.out.println(", class" + this.getClass());
            } while (records.size() > 0);
        }
        return getResult();
    }

    protected abstract T getResult();

    public void setSubject(ReviewSubject subject) {
        this.subject = subject;
    }

    public void handle() {
        List<String[]> recs = subject.getRecords();
        if (!recs.equals(records)) {
            records = recs;
            synchronized (this) {
                notify();
            }
        }
    }
}
