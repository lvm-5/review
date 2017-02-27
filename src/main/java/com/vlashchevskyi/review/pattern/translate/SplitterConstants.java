package com.vlashchevskyi.review.pattern.translate;

/**
 * Created by lvm on 2/20/17.
 */
public interface SplitterConstants {
    String BLOCK_SPLITTER = ".";
    String REVIEW_SPLITTER = "[^\\w^\\s^'^\\d]";
    String REVIEW_ANTI_SPLITTER = "[\\w\\s'\\d]";
}
