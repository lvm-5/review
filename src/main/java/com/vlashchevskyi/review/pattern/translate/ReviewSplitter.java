package com.vlashchevskyi.review.pattern.translate;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.BLOCK_SPLITTER_SIZE;
import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.REVIEW_SPLITTER;

/**
 * Created by lvm on 2/16/17.
 */
public class ReviewSplitter {
    private int blockSize = 1000;

    public Set<String> split2Phrases(String review) {
        String[] phrases = review.split(REVIEW_SPLITTER);
        return Arrays.stream(phrases).map(p -> p.trim()).filter(p -> !p.isEmpty()).collect(Collectors.toSet());
    }

    public Set<String> split2Phrases(List<String> reviews) {
        Set<String> phrases = new ConcurrentSkipListSet<>();
        reviews.parallelStream().forEach(review->  {
            Set<String> phrasesByReview = split2Phrases(review);
            phrases.addAll(phrasesByReview);
        });

        return phrases.parallelStream().sorted().collect(Collectors.toSet());
    }

    public List<List<String>> split2Blocks(Set<String> phrases) {
        List<List<String>> blocks = new ArrayList();
        while (phrases.size() > 0) {
            List<String> block = buildBlock(phrases.stream().collect(Collectors.toList()), blockSize);
            blocks.add(block);
            phrases = phrases.stream().filter(p->!block.contains(p)).collect(Collectors.toSet());
        }

        return blocks;
    }

    public List<String> buildBlockBySeq(Iterator<String> it, long sum, long limit) {
        List<String> block = null;
        if (it.hasNext()) {
            String phrase = it.next();
            long size = sum + phrase.length() + BLOCK_SPLITTER_SIZE;
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
                long size = primer.length() + BLOCK_SPLITTER_SIZE;
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

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}
