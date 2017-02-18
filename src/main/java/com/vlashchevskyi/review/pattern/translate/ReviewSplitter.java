package com.vlashchevskyi.review.pattern.translate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lvm on 2/16/17.
 */
public class ReviewSplitter {
    public static final String SPLITTER = "[^\\w^\\s]";

    public Set<String> split2phrase(String review) {
        String[] phrases = review.split(SPLITTER);
        return Arrays.stream(phrases).map(p -> p.trim()).filter(p -> !p.isEmpty()).collect(Collectors.toSet());
    }


    public List<String> buildBlockBySeq(Iterator<String> it, long sum, long limit) {
        List<String> block = null;
        if (it.hasNext()) {
            String phrase = it.next();
            long size = sum + phrase.length();
            if (size < limit) {
                block = buildBlockBySeq(it, size, limit);
            } else if (size == limit) {
                block = new ArrayList<>();
            }

            if (block != null) {
                block.add(phrase);
            }
        }

        return block;
    }

    public List<String> buildBlock(List<String> phrases, long limit) {
        List<String> block = null;

        for(;block == null && limit > 0; --limit) {
            for (int j = 0; block == null && j < phrases.size(); j++) {
                String primer = phrases.get(j);
                long size = primer.length();
                for (int i = j + 1; block == null && i < phrases.size(); i++) {
                    Iterator it = phrases.subList(i, phrases.size()).iterator();
                    block = buildBlockBySeq(it, size, limit);
                }

                if (block != null) {
                    block.add(primer);
                }
            }
        }

        return block;
    }
}
