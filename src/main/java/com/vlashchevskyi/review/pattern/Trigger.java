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
public class Trigger<T extends ReviewTaskObserver<Object, List> > {
    private final ExecutorService pool;
    private ReviewSubject subject = new ReviewSubject();

    public void setSubject(ReviewSubject subject) {
        this.subject = subject;
    }

    public synchronized List<Future<Object>> trigger(List<T> tasks)
            throws IOException, ExecutionException, InterruptedException {

        // starter task should be first
        ReviewTaskObserver starter = tasks.get(0);

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

    private synchronized List<Future<Object>> submit(List<T> tasks) throws InterruptedException {
        List<Future<Object>> fes = new ArrayList<>();
        tasks.forEach(t->fes.add(pool.submit(t)));

        return fes;
    }

    public Trigger(int taskAmount) {
        pool = Executors.newFixedThreadPool(taskAmount);
    }
}
