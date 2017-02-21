package com.vlashchevskyi.review.pattern.translate;

import com.google.cloud.translate.Translate;
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
public class TranslateTask<T extends List<String>> extends ReviewTaskObserver<T> {
    private static final ReviewSplitter splitter = new ReviewSplitter();

    private final Translate.TranslateOption srcLang;
    private final Translate.TranslateOption trgtLang;
    private final ExecutorService pool;

    private int connectionLimit;
    private Translate service;
    private T sourceReviews;
    private T targetReviews = (T) new ArrayList<String>();

    public T doTranslate() throws InterruptedException {
        sourceReviews = (T) getRecords().map(r -> r[TEXT_COLUMN]).collect(Collectors.toList());
        Set<String> phrases = splitter.split2Phrases(sourceReviews);
        List<List<String>> blocks = splitter.split2Blocks(phrases);

        List<TranslateRequestTask<Map<String, String>>> requests = new ArrayList<>();
        Map<String, String> dictionary = new HashMap<>();
        for (int i = 0; i < blocks.size(); i++) {
            requests.add(new TranslateRequestTask(blocks.get(i), service, srcLang, trgtLang));
            if (requests.size() == connectionLimit || i == blocks.size() - 1) {
                fillDictionary(doRequests(requests), dictionary);
                requests = new ArrayList<>();
            }
        }

        T targetReviews = translate(sourceReviews, dictionary);
        aggregate(targetReviews);

        return targetReviews;
    }

    private synchronized List<Future<Map<String, String>>> doRequests(List<TranslateRequestTask<Map<String, String>>> requests) throws InterruptedException {
        List<Future<Map<String, String>>> fes = pool.invokeAll(requests);
        while (fes.stream().anyMatch(f -> !f.isDone())) {
            wait(200);
        }

        return fes;
    }

    private void aggregate(T currentReviews) {
        targetReviews.addAll(currentReviews);
    }


    //TODO: make more effective
    private T translate(List<String> reviews, Map<String, String> dictionary) {
        T target = (T) new ArrayList<String>();

        reviews.parallelStream().forEach(r -> {
            String review = r;
            Set<String> phrases = splitter.split2Phrases(r);
            Iterator<String> it = phrases.iterator();
            while (it.hasNext()) {
                String p = it.next();
                review = review.replaceAll(p, dictionary.get(p));
            }
            target.add(review);
        });

        return target;
    }

    private void fillDictionary(List<Future<Map<String, String>>> fes, Map<String, String> dictionary) {
        fes.forEach(f -> {
            try {
                dictionary.putAll(f.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public T doAction() throws Exception {
        return doTranslate();
    }

    @Override
    protected T getResult() {
        return targetReviews;
    }

    public TranslateTask(Translate service, int connectionLimit) {
        this.service = service;
        this.connectionLimit = connectionLimit;
        pool = Executors.newFixedThreadPool(this.connectionLimit);
        srcLang = Translate.TranslateOption.sourceLanguage("en");
        trgtLang = Translate.TranslateOption.targetLanguage("fr");
    }
}
