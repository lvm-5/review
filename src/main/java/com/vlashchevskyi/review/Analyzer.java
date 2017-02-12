package com.vlashchevskyi.review;

import java.util.Collection;

/**
 * Created by lvm on 2/8/17.
 */
public abstract class Analyzer<T> {
    protected final T records;

    // required to show first records at analyzing
    protected int amount = 10;

    public abstract <U>U calc() throws InterruptedException;

    public Analyzer(T records) {
        this.records = records;
    }

    protected  <T extends Collection> int fixAmount(T items, int amount) {
        return (items.size() < amount) ? items.size() : amount;
    }


}
