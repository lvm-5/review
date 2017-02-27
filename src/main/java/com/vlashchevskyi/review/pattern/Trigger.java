package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;
import com.vlashchevskyi.tool.Delay;

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


    public synchronized List<Future<Object>> trigger(List<ReviewTaskObserver<Object>> tasks) throws IOException, ExecutionException, InterruptedException {

        // starter task should be first
        ReviewTaskObserver starter = tasks.get(0);
        ReviewSubject subject = new ReviewSubject();
        tasks.forEach(t -> subject.addTask(t));

        List<Future<Object>> fes = null;
        Delay dl = new Delay();
        try {
            fes = submit(tasks);
            do {
                if (!dl.doPauseIf(subject.getReadyCounter() != subject.getTasksAmount())) {
                    subject.start(starter);
                }
            } while (fes.stream().anyMatch(f -> !f.isDone()));
        } finally {
            pool.shutdown();
        }

        return fes;
    }

    private synchronized List<Future<Object>> submit(List<ReviewTaskObserver<Object>> tasks) throws InterruptedException {
        List<Future<Object>> fes = new ArrayList<>();
        tasks.forEach(t->fes.add(pool.submit(t)));

        return fes;
    }

    public Trigger(int taskAmount) {
        pool = Executors.newFixedThreadPool(taskAmount);
    }
}
