package com.vlashchevskyi.review.pattern.translate;

import com.vlashchevskyi.review.pattern.task.ReviewTask;

import java.util.List;
import java.util.Map;

/**
 * Created by lvm on 2/17/17.
 */
public class TranslateTask<T extends Map<String, String>> implements ReviewTask<T>{
    private final List<String>block;

    public TranslateTask(List<String> block) {
        this.block = block;
    }

    public T doTranslate() {
        return null;
    }

    @Override
    public T doAction() throws Exception {
        return doTranslate();
    }

    @Override
    public T call() throws Exception {
        return doAction();
    }
}
