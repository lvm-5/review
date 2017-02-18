package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by lvm on 2/6/17.
 */
public class Runner {
    private int topAmount = 1000;
    private final ExecutorService pool;
    private final ReviewPrinter printer;

    public static void main(String[] args) {
        new Runner().handle("Reviews.csv");
    }

    public boolean handle(String pathToReviews) {
        boolean status = false;

        try {
            List<ReviewTaskObserver> tasks = prepareTasks(pathToReviews);
            List<Future> result = new Trigger(tasks.size()).trigger(tasks);
            print(result);
            status = true;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    private List<ReviewTaskObserver> prepareTasks(String pathToReviews) throws IOException {
        List<ReviewTaskObserver> tasks = new ArrayList<>();
        tasks.add(new ReadReviewTask(pathToReviews));
        tasks.add(new GetTopUsersTask());       // Task #1
        tasks.add(new GetTopItemsTask());       // Task #2
        tasks.add(new GetTopWordsTask());       // Task #3

        return tasks;
    }

    private void print(List<Future> result) {
        Map<String, Future> subjects = new LinkedHashMap<>();

        subjects.put("Top of the most active users", result.get(1));
        subjects.put("Top of the most commented products", result.get(2));
        subjects.put("Top of the most used words", result.get(3));
        printer.printAll(subjects, topAmount);
    }

    public void setAMOUNT(int amount) {
        this.topAmount = amount;
    }

    public Runner() {
        pool = Executors.newFixedThreadPool(5);
        printer = new ReviewPrinter();
    }
}