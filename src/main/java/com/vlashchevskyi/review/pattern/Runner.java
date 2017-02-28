package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.*;
import com.vlashchevskyi.tool.memory.MemoryMonitorTask;
import com.vlashchevskyi.review.pattern.translate.GoogleTranslator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by lvm on 2/6/17.
 */
public class Runner<T extends ReviewTaskObserver<Object, List>> {
    private int topAmount = 1000;
    private String pathToReviews;

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


        Thread memoDmn = new Thread(new MemoryMonitorTask());
        memoDmn.setDaemon(true);
        memoDmn.start();

        try {
            status = calculateTop();
            status = translateFlag
                    ? translate()
                    : status;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        memoDmn.interrupt();

        return status;
    }

    private boolean calculateTop()
            throws InterruptedException, ExecutionException, IOException {

        List<T> tasks = prepareTasks();
        List<Future<Object>> result = new Trigger(tasks.size()).trigger(tasks);
        print(result);

        return true;
    }

    private boolean translate() throws Exception {
        new GoogleTranslator().doTranslate(pathToReviews);

        return true;
    }

    private List<T> prepareTasks() throws IOException {
        List<T> tasks = new ArrayList<>();
        tasks.add((T)new ReadReviewTask(pathToReviews));
        tasks.add((T)new GetTopUsersTask());       // Task #1
        tasks.add((T)new GetTopItemsTask());       // Task #2
        tasks.add((T)new GetTopWordsTask());       // Task #3

        return tasks;
    }

    private void print(List<Future<Object>> result) {
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
        printer = new ReviewPrinter();
        this.pathToReviews = pathToReviews;

    }
}