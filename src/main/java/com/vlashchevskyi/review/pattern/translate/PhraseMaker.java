package com.vlashchevskyi.review.pattern.translate;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.REVIEW_SPLITTER;

/**
 * Created by lvm on 2/16/17.
 */
public class PhraseMaker {
    private int blockSize;

    public Set<String> doPhraseByPattern(String review) {
        Set<String> phrases = new HashSet<>();
        String[] phs = review.split(REVIEW_SPLITTER);

        for (String ph : phs) {
            String phrase = ph.trim();
            if (phrase.length() > blockSize) {
                phrases.addAll(doPhraseByPos(phrase));
            } else if (!phrase.isEmpty()) {
                phrases.add(phrase);
            }
        }

        return phrases;
    }

    public Set<String> doPhraseByPattern(List<String> reviews) {
        Set<String> phrases = new ConcurrentSkipListSet<>();
        reviews.parallelStream().forEach(review->  {
            Set<String> phrasesByReview = doPhraseByPattern(review);
            phrases.addAll(phrasesByReview);
        });

        return phrases.parallelStream().sorted().collect(Collectors.toSet());
    }

    public List<String> doPhraseByPos(String phrase) {
        List<String> phs = new ArrayList<>();

        int start = 0;
        int end = blockSize;
        do {
            phs.add(phrase.substring(start, end));
            start = end;
            end = start + blockSize;
            end = (end > phrase.length()) ? phrase.length() : end;
        }while (start != end);

        return phs;
    }


    public List<String> gatherSplitters(String reivew) {
        return Arrays.stream(reivew
                .split(SplitterConstants.REVIEW_ANTI_SPLITTER))
                .filter(s->!s.isEmpty()).collect(Collectors.toList());
    }



    public PhraseMaker(int blockSize) {
        this.blockSize = blockSize;
    }
}
