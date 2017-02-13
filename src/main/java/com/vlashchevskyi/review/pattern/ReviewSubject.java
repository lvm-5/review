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
    private final String lock;

    public void addTask(ReviewTaskObserver task) {
        task.setSubject(this);
        tasks.add(task);
    }

    public void removeTask(ReviewTaskObserver task) {
        task.setSubject(null);
        tasks.remove(task);
    }

    public synchronized void updateReadyCounter() {
        readyCounter++;
    }

    public void start(ReviewTaskObserver task) {
        synchronized (task) {
            readyCounter = 0;
            task.notify();
        }
    }

    public void update() {
        tasks.forEach(t-> t.handle());
    }

    public synchronized Integer getReadyCounter() {
        return readyCounter;
    }

    public void setRecords(List<String[]> records) {
        synchronized (lock) {
            this.records = records;
            update();
        }
    }

    public List<String[]> getRecords() {
        synchronized (lock) {
            return records;
        }
    }

    public int getTasksAmount() {
        return tasks.size();
    }

    public ReviewSubject() {
        lock = new String("records");
    }
}
