package com.vlashchevskyi.review.pattern.translate;

import com.vlashchevskyi.review.pattern.Trigger;
import com.vlashchevskyi.review.pattern.task.ReadReviewTask;
import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lvm on 2/16/17.
 */
public class GoogleTranslator {

    public void doTranslate(String pathToReviews) throws Exception {
        List<ReviewTaskObserver> tasks = new ArrayList<>();
        tasks.add(new ReadReviewTask(pathToReviews));
        tasks.add(new TranslateTask());
        new Trigger(tasks.size()).trigger(tasks);
    }
}
