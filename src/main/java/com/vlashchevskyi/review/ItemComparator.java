package com.vlashchevskyi.review;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by lvm on 2/3/17.
 */
public class ItemComparator implements Comparator<String> {

    private Map<String, Integer> map;

    public ItemComparator(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public int compare(String t1, String t2) {
        Integer value1 = map.get(t1);
        Integer value2 = map.get(t2);
        int status = value2.compareTo(value1);
        status = (status == 0)? t1.compareTo(t2): status;

        return status;
    }
}

