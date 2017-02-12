package com.vlashchevskyi.review.pattern.task;

import com.vlashchevskyi.review.pattern.ReviewTaskObserver;

import java.util.*;

import static com.vlashchevskyi.review.pattern.ReviewConstants.SUMMARY_COLUMN;
import static com.vlashchevskyi.review.pattern.ReviewConstants.TEXT_COLUMN;

/**
 * Created by lvm on 2/10/17.
 */
public class GetTopWordsTask<U, T extends Map<String, Integer>>  extends ReviewTaskObserver {
    private final T topItems;

    @Override
    public T doAction() throws Exception {
        T wordsStat =  analyze();
        aggregate(wordsStat);

        return wordsStat;
    }

    @Override
    protected U getResult() {
        return (U) topItems.entrySet().stream().sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
    }

    public T analyze() {
        final String pattern = "\\W|\\d";
        final T wordStatistics = (T) new HashMap<String, Integer>();

        List<String[]> rs = records;
        for (String[] record : rs) {
            String body = getReviewText(record);
            String[] words = body.toLowerCase().split(pattern);
            for (String word : words) {
                if (!word.isEmpty()) {
                    Integer sum = (wordStatistics.get(word));
                    wordStatistics.put(word, (sum == null) ? 1: ++sum);
                }
            }
        }

        return wordStatistics;
    }

    private void aggregate(T words) {
        words.forEach((id, sum)->{
            Integer sm = topItems.get(id);
            topItems.put(id, sm != null? sm + sum: sum);
        });
    }

    private String getReviewText(String[] record) {
        return new StringBuilder(record[SUMMARY_COLUMN]).append(record[TEXT_COLUMN]).toString();
    }

    public GetTopWordsTask() {
        topItems = (T) new HashMap<String, Integer>();
    }
}
