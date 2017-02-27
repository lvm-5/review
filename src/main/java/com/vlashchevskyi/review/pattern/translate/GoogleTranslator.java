package com.vlashchevskyi.review.pattern.translate;

import com.google.cloud.RetryParams;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.vlashchevskyi.review.pattern.Trigger;
import com.vlashchevskyi.review.pattern.task.ReadReviewTask;
import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;
import com.vlashchevskyi.review.pattern.test.Emulator;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lvm on 2/16/17.
 */
public class GoogleTranslator implements Emulator {
    private int connectionLimit;

    public boolean doTranslate(String pathToReviews) throws Exception {
        List<ReviewTaskObserver<Object>> tasks = new ArrayList<>();
        tasks.add(new ReadReviewTask(pathToReviews));
        tasks.add(new TranslateTask(createTranslateService(), connectionLimit));
        new Trigger(tasks.size()).trigger(tasks);

        return true;
    }

    /**
     * Create Google Translate API Service.
     *
     * @return Google Translate Service
     */
    public static Translate createTranslateService() {
        TranslateOptions translateOption = TranslateOptions.newBuilder()
                .setHost(emulator.getHost())
                .setRetryParams(retryParams())
                .setConnectTimeout(60000)
                .setReadTimeout(60000)
                .build();

        return translateOption.getService();
    }

    /**
     * Retry params for the Translate API.
     */
    private static RetryParams retryParams() {
        return RetryParams.newBuilder()
                .setRetryMaxAttempts(3)
                .setMaxRetryDelayMillis(30000)
                .setTotalRetryPeriodMillis(120000)
                .setInitialRetryDelayMillis(250)
                .build();
    }

    public GoogleTranslator() {
        connectionLimit = 100;
    }
}
