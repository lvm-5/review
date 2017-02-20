package com.vlashchevskyi.review.pattern.translate;

import com.google.cloud.RetryParams;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static com.vlashchevskyi.review.pattern.ReviewConstants.TEXT_COLUMN;

/**
 * Created by lvm on 2/17/17.
 */
public class TranslateTask<T extends List<String>> extends ReviewTaskObserver<T>{
    public static final int CONNECTION_LIMIT = 100;
    private static final ReviewSplitter splitter = new ReviewSplitter();


    private Translate service;
    private final ExecutorService pool;
    private T sourceReviews;
    private T targetReviews = (T)new ArrayList<String>();

    public T doTranslate() throws InterruptedException {
        sourceReviews = (T)getRecords().map(r->r[TEXT_COLUMN]).collect(Collectors.toList());
        Set<String> phrases = splitter.split2Phrases(sourceReviews);
        List<List<String>> blocks = splitter.split2Blocks(phrases);

        List<TranslateRequestTask<Map<String, String>>> requests;
        Map<String, String> dictionary = new HashMap<>();
        do {
            requests = new ArrayList<>();
            for (int i = 0; i < blocks.size(); i++) {
                requests.add(new TranslateRequestTask(blocks.get(i), service));
                if (requests.size() == CONNECTION_LIMIT) {
                    fillDictionary(doRequests(requests), dictionary);
                }
            }
        }while(requests.size() > 0);

        targetReviews = translate(sourceReviews, dictionary);

        return targetReviews;
    }

    private T translate(List<String> reviews, Map<String, String> dictionary) {

        return null;
    }

    private synchronized List<Future<Map<String,String>>> doRequests(List<TranslateRequestTask<Map<String, String>>> requests) throws InterruptedException {
        List<Future<Map<String,String>>> fes = pool.invokeAll(requests);
        while(fes.stream().anyMatch(f->!f.isDone())) {
            wait(200);
        }

        return fes;
    }

    private void fillDictionary(List<Future<Map<String,String>>> fes, Map<String, String> dictionary) {
        fes.forEach(f -> {
            try {
                dictionary.putAll(f.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Create Google Translate API Service.
     *
     * @return Google Translate Service
     */
    public static Translate createTranslateService() {
        TranslateOptions translateOption = TranslateOptions.newBuilder()
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

    @Override
    public T doAction() throws Exception {
        return doTranslate();
    }

    @Override
    protected T getResult() {
        return null;
    }

    public TranslateTask() {
        pool = Executors.newFixedThreadPool(CONNECTION_LIMIT);
        service = createTranslateService();
    }
}
