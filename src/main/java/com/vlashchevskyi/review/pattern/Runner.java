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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void trigger() throws IOException, ExecutionException, InterruptedException {
        pool = Executors.newFixedThreadPool(5);

        ReviewSubject subject = new ReviewSubject();
        List<Future> fes = new ArrayList<>();

        List<ReviewTaskObserver> tasks = new ArrayList<>();
        tasks.add(new ReadReviewTask("Reviews.csv"));
        tasks.add(new GetTopItemsTask()); // TODO: TASK = 1
        tasks.add(new GetTopUsersTask()); // TODO: TASK = 2
        tasks.add(new GetTopWordsTask()); // TODO: TASK = 3
        tasks.forEach(t -> {
            subject.addTask(t);
            fes.add(pool.submit(t));
        });

        try {
            ReviewTaskObserver readTask = tasks.get(0);
            subject.start(readTask);
            do {
                if (subject.getReadyCounter() == subject.getTasksAmount()) {
                    subject.resetCounter();
                    subject.start(readTask);
                }
            } while (fes.stream().anyMatch(f -> !f.isDone()));
        } finally {
            pool.shutdown();
            printAll(fes, AMOUNT);
        }
    }
    public void printAll(List<Future> fes, int amount) throws ExecutionException, InterruptedException {
        final int ITEM_TASK = 1;
        final int USER_TASK = 2;
        final int WORD_TASK = 3;

        printer.print(fes.get(USER_TASK), amount, "Top of the most active users");
        printer.print(fes.get(ITEM_TASK), amount, "Top of the most commented products");
        printer.print(fes.get(WORD_TASK), amount, "Top of the most used words");
    }
}