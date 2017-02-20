package com.vlashchevskyi.review.pattern.translate;

/**
 * Created by lvm on 2/20/17.
 */
public interface SplitterConstants {
    String BLOCK_SPLITTER = ".";
    String REVIEW_SPLITTER = "[^\\w^\\s^'^\\d]";
    int BLOCK_SPLITTER_SIZE = BLOCK_SPLITTER.length();
    int BLOCK_SIZE = 1000;
}
