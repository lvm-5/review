package com.vlashchevskyi.review.pattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvm on 2/11/17.
 */
public class ReviewSubject {
    private List<String[]> records = new ArrayList<>();
    private List<ReviewTaskObserver> tasks = new ArrayList<>();
    private int readyCounter = 0;

    public void setRecords(List<String[]> records) {
        this.records = records;
        update();
    }

    public Integer getReadyCounter() {
        return readyCounter;
    }

    public int getTasksAmount() {
        return tasks.size();
    }

    public void updateCounter() {
        synchronized (this) {
            readyCounter++;
        }
    }

    public void resetCounter() {
        synchronized (this) {
            readyCounter = 0;
        }
    }

    public List<String[]> getRecords() {
        return records;
    }

    public void addTask(ReviewTaskObserver task) {
        task.setSubject(this);
        tasks.add(task);
    }

    public void removeTask(ReviewTaskObserver task) {
        task.setSubject(null);
        tasks.remove(task);
    }

    public void update() {
        tasks.forEach(t-> t.handle());
    }

    public void start(ReviewTaskObserver task) {
        synchronized (task) {
            task.notify();
        }

    }
}
