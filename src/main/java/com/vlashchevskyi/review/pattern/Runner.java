package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.*;
import com.vlashchevskyi.review.pattern.translate.GoogleTranslator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by lvm on 2/6/17.
 */
public class Runner {
    private int topAmount = 1000;
    private String pathToReviews;

    private final ExecutorService pool;
    private final ReviewPrinter printer;

    private static final String TRANSLATE = "translate";
    private static final String PATH = "pathToReviews";


    public static void main(String[] args) {
        Map<String, String> as = Arrays
                .stream(args)
                .map(a -> a.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a[1]));

        String path = as.get(PATH);
        path = (path == null) ? "Reviews.csv" : path;
        Boolean translate = Boolean.valueOf(as.get(TRANSLATE));

        new Runner(path).handle(translate);
    }

    public boolean handle(boolean translateFlag) {
        boolean status;

        try {
            status = calculateTop();
            status = translateFlag
                    ? translate()
                    : status;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    private boolean calculateTop() throws InterruptedException, ExecutionException, IOException {
        List<ReviewTaskObserver> tasks = prepareTasks();
        List<Future> result = new Trigger(tasks.size()).trigger(tasks);
        print(result);

        return true;
    }

    private boolean translate() throws Exception {
        new GoogleTranslator().doTranslate(pathToReviews);

        return true;
    }

    private List<ReviewTaskObserver> prepareTasks() throws IOException {
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

    public Runner(String pathToReviews) {
        pool = Executors.newFixedThreadPool(5);
        printer = new ReviewPrinter();
        this.pathToReviews = pathToReviews;

    }
}