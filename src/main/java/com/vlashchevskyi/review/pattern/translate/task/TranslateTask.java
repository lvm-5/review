package com.vlashchevskyi.review.pattern.translate.task;

import com.google.cloud.translate.Translate;
import com.vlashchevskyi.review.pattern.Trigger;
import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;
import com.vlashchevskyi.review.pattern.translate.PhraseMaker;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.vlashchevskyi.review.pattern.ReviewConstants.SUMMARY_COLUMN;

/**
 * Created by lvm on 2/17/17.
 */
public class TranslateTask<T extends List<String>
        , U extends List<String[]>> extends ReviewTaskObserver<T, U> {


    private final Translate.TranslateOption srcLang;
    private final Translate.TranslateOption trgtLang;
    private final int connectionLimit;

    private T targetReviews = (T) new ArrayList<String>();
    private Translate service;


    public T translateAll() throws InterruptedException, IOException, ExecutionException {
        List<TranslateTaskObserver> tasks = new ArrayList();
        List<String> sourceReviews = makeSourceReviews(getResource());
        tasks.add(new BuildBlockTask(sourceReviews));
        for (int i = 0; i < connectionLimit ; i++) {
            tasks.add(new TranslateRequestTask<>(service, srcLang, trgtLang));
        }

        Trigger starter = new Trigger<>(tasks.size());

        List<Future<Map<String, String>>> fes = starter.trigger(tasks);
        Map<String, String> dictionary = makeDictionary(fes.subList(1, fes.size()));

        T targetReviews = translate(sourceReviews, dictionary);
        aggregate(targetReviews);

        return targetReviews;
    }

    private T makeSourceReviews(U records) {
        T reviews = (T)new LinkedList<String>();
        records.forEach(r-> reviews.add(r[SUMMARY_COLUMN]));

        return reviews;
    }

    private <M extends Map<String, String>> M makeDictionary(List<Future<M>> fes) {
        M dictionary = (M)new HashMap<String, String>();
        fes.forEach(f -> {
            try {
                dictionary.putAll(f.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return dictionary;
    }

    @Override
    public T doAction() throws Exception {
        return translateAll();
    }

    @Override
    protected T getResult() {
        return targetReviews;
    }


    private void aggregate(T currentReviews) throws InterruptedException {
        targetReviews.addAll(currentReviews);
    }


    //TODO: make more effective
    private T translate(List<String> reviews, Map<String, String> dictionary) {
        T target = (T) new ArrayList<String>();
        reviews.forEach(r -> {
            String review = r;
            Set<String> phrases = new PhraseMaker().doPhraseByPattern(r);

            Iterator<String> it = phrases.iterator();
            while (it.hasNext()) {
                String p = it.next();
                String t = dictionary.get(p);
                if (t != null && !t.isEmpty()){
                    review = review.replaceAll(p, t);
                }
            }
            target.add(review);
        });
         return target;
    }


    public TranslateTask(Translate service, int connectionLimit) {
        this.service = service;
        this.connectionLimit = connectionLimit;
        srcLang = Translate.TranslateOption.sourceLanguage("en");
        trgtLang = Translate.TranslateOption.targetLanguage("fr");
    }
}
