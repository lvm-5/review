package com.vlashchevskyi.review.pattern.task;

import java.util.HashMap;
import java.util.Map;

import static com.vlashchevskyi.review.pattern.ReviewConstants.SUMMARY_COLUMN;
import static com.vlashchevskyi.review.pattern.ReviewConstants.TEXT_COLUMN;

/**
 * Created by lvm on 2/10/17.
 */
public class GetTopWordsTask<T extends Map<String, Integer>> extends GetTopItemsTask {

    @Override
    public T analyze() {
        final String PATTERN = "\\W|\\d";
        final T wordStatistics = (T) new HashMap<String, Integer>();

        getRecords().forEach(record -> {;
            String body = getReviewText((String[])record);
            String[] words = body.toLowerCase().split(PATTERN);
            for (String word : words) {
                if (!word.isEmpty()) {
                    Integer sum = (wordStatistics.get(word));
                    wordStatistics.put(word, (sum == null) ? 1: ++sum);
                }
            }
        });

        return wordStatistics;
    }

    private String getReviewText(String[] record) {
        return new StringBuilder(record[SUMMARY_COLUMN]).append(record[TEXT_COLUMN]).toString();
    }
}
