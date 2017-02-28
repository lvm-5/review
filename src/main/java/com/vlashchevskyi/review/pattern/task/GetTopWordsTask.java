package com.vlashchevskyi.review.pattern.task;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.vlashchevskyi.review.pattern.ReviewConstants.SUMMARY_COLUMN;
import static com.vlashchevskyi.review.pattern.ReviewConstants.TEXT_COLUMN;

/**
 * Created by lvm on 2/10/17.
 */
public class GetTopWordsTask<T extends Map<String, Integer>> extends GetTopItemsTask {
    public static final String PATTERN = "\\W|\\d";

    @Override
    public T analyze() {

        final T wordStatistics = (T) new HashMap();

        getResource().forEach(record -> {
            String body = getReviewText((String[]) record);
            Arrays.stream(body.toLowerCase().split(PATTERN))
                    .forEach(word -> {
                        if (!word.isEmpty()) {
                            Integer sum = (wordStatistics.get(word));
                            sum = (sum == null)? 1: ++sum;
                            wordStatistics.put(word, sum);
                        }
                    });
        });

        return wordStatistics;
    }

    private String getReviewText(String[] record) {
        return new StringBuilder(record[SUMMARY_COLUMN]).append(record[TEXT_COLUMN]).toString();
    }
}
