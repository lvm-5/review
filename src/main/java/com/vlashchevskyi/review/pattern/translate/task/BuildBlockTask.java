package com.vlashchevskyi.review.pattern.translate.task;

import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;
import com.vlashchevskyi.review.pattern.translate.BlockMaker;
import com.vlashchevskyi.review.pattern.translate.PhraseMaker;
import com.vlashchevskyi.review.pattern.translate.PhraseMap;

import java.util.List;
import java.util.Set;

/**
 * Created by lvm on 2/10/17.
 */
public class BuildBlockTask<T extends List<List<String>>, U extends List> extends ReviewTaskObserver<T, U> {
    private T result;
    private List<String> reviews;

    @Override
    public synchronized T doAction() throws Exception {
        if (result == null) {
            T blocks = build();
            result = blocks;
            subject.setResource((U) blocks);

        }

        return result;
    }


    public T build() throws Exception {
        Set<String> phrases = new PhraseMaker().doPhraseByPattern(reviews);
        PhraseMap map = new PhraseMap(phrases);

        return (T) new BlockMaker(map).buildBlocks();
    }

    @Override
    protected T getResult() {
        return result;
    }

    public BuildBlockTask(List<String> reviews) {
        this.reviews = reviews;
    }


}
