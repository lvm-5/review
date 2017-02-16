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
 * Created by lvm on 2/6/17.
 */
public class Runner {
    private int amount = 1000;
    private final ExecutorService pool;
    private final ReviewPrinter printer;

    public static void main(String[] args) {
        try {
            new Runner().trigger(new ReadReviewTask("Reviews.csv"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trigger(ReviewTaskObserver readTask) throws IOException, ExecutionException, InterruptedException {
        List<ReviewTaskObserver> tasks = new ArrayList<>();
        tasks.add(readTask);
        tasks.add(new GetTopItemsTask());
        tasks.add(new GetTopUsersTask());
        tasks.add(new GetTopWordsTask());
        tasks.forEach(t -> t.setTestMode(readTask.getTestMode()));

        ReviewSubject subject = new ReviewSubject();
        tasks.forEach(t -> subject.addTask(t));

        List<Future> fes = null;
        try {
            fes = submit(tasks);
            subject.start(readTask);
            do {
                if (subject.getReadyCounter() == subject.getTasksAmount()) {
                    subject.start(readTask);
                }
            } while (fes.stream().anyMatch(f -> !f.isDone()));
        } finally {
            pool.shutdown();
            printer.printAll(fes, amount);
        }
    }

    private synchronized List<Future> submit(List<ReviewTaskObserver> tasks) throws InterruptedException {
        List<Future> fes = new ArrayList<>();
        tasks.forEach(t -> {
            fes.add(pool.submit(t));
        });
        wait(5000);

        return fes;
    }

    public void setAMOUNT(int amount) {
        this.amount = amount;
    }

    public Runner() {
        pool = Executors.newFixedThreadPool(5);
        printer = new ReviewPrinter();
    }
}