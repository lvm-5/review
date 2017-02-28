package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lvm on 2/11/17.
 */
public class ReviewSubject<T extends List> {
    private T resource = (T) new ArrayList();

    private List<ReviewTaskObserver> tasks = new LinkedList<>();
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

    public void update() {
        tasks.forEach(t-> t.handle());
    }

    public void start(ReviewTaskObserver task) {
        synchronized (task) {
            readyCounter = 0;
            task.notify();
        }
    }

    public void setResource(T resource) {
        synchronized (lock) {
            this.resource = resource;
            update();
        }
    }

    public T getResource() {
        synchronized (lock) {
            return resource;
        }
    }

    public List<ReviewTaskObserver> getTasks() {
        return tasks;
    }

    public synchronized void updateReadyCounter() {
        readyCounter++;
    }

    public synchronized Integer getReadyCounter() {
        return readyCounter;
    }

    public int getTasksAmount() {
        return tasks.size();
    }

    public ReviewSubject() {
        lock = new String("resource");
    }
}
