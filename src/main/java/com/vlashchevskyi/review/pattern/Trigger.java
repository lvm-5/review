package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lvm on 2/18/17.
 */
public class Trigger {
    private final ExecutorService pool;


    public List<Future> trigger(List<ReviewTaskObserver> tasks) throws IOException, ExecutionException, InterruptedException {

        // starter task should be first
        ReviewTaskObserver starter = tasks.get(0);
        ReviewSubject subject = new ReviewSubject();
        tasks.forEach(t -> subject.addTask(t));

        List<Future> fes = null;
        try {
            fes = submit(tasks);
            subject.start(starter);
            do {
                if (subject.getReadyCounter() == subject.getTasksAmount()) {
                    subject.start(starter);
                }
            } while (fes.stream().anyMatch(f -> !f.isDone()));
        } finally {
            pool.shutdown();
        }

        return fes;
    }

    private synchronized List<Future> submit(List<ReviewTaskObserver> tasks) throws InterruptedException {
        List<Future> fes = new ArrayList<>();
        tasks.forEach(t -> {
            fes.add(pool.submit(t));
        });
        wait(5000);

        return fes;
    }

    public Trigger(int taskAmount) {
        pool = Executors.newFixedThreadPool(taskAmount);
    }
}
