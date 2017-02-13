package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.GetTopItemsTask;
import com.vlashchevskyi.review.pattern.task.GetTopUsersTask;
import com.vlashchevskyi.review.pattern.task.GetTopWordsTask;
import com.vlashchevskyi.review.pattern.task.ReadReviewTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lvm on 2/6/17.
 *
 */
public class Runner {
    private final int AMOUNT = 1000;
    private ExecutorService pool = null;
    private ReviewPrinter printer = new ReviewPrinter();

    public static void main(String[] args) {
        try {
            new Runner().trigger();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trigger() throws IOException, ExecutionException, InterruptedException {
        List<ReviewTaskObserver> tasks = new ArrayList<>();
        ReviewTaskObserver readTask = new ReadReviewTask("Reviews.csv");
        tasks.add(readTask);
        tasks.add(new GetTopItemsTask()); // TODO: TASK = 1
        tasks.add(new GetTopUsersTask()); // TODO: TASK = 2
        tasks.add(new GetTopWordsTask()); // TODO: TASK = 3

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

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
            printer.printAll(fes, AMOUNT);
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

    public Runner() {
        pool = Executors.newFixedThreadPool(5);
    }
}