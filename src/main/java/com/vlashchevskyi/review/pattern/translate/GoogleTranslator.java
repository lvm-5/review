package com.vlashchevskyi.review.pattern.translate;

import com.vlashchevskyi.review.pattern.task.ReadReviewTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by lvm on 2/16/17.
 */
public class GoogleTranslator {
    private ExecutorService pool = Executors.newFixedThreadPool(2);
    private int connectionLimit = 100;

    public void doTranslate(String pathToReviews) throws Exception {
        ReadReviewTask readTask = new ReadReviewTask(pathToReviews);
        //TranslateTask translateTask = new TranslateTask();
    }
}
