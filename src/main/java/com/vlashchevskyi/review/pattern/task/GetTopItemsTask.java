package com.vlashchevskyi.review.pattern.task;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.vlashchevskyi.review.pattern.ReviewConstants.PRODUCT_ID_COLUMN;

/**
 * Created by lvm on 2/10/17.
 */
public class GetTopItemsTask<K, T extends Map<K, Integer>> extends ReviewTaskObserver<T> {
    private final T topItems;

    @Override
    public T doAction() throws Exception {
        T itemStat = analyze();
        aggregate(itemStat);
        return itemStat;
    }

    protected T analyze() throws Exception {
        return sumByColumn(PRODUCT_ID_COLUMN);
    }

    private void aggregate(T items) {
        items.forEach((id, sum)->{
            Integer sm = topItems.get(id);
            sm = (sm != null)? sm + sum: sum;
            topItems.put(id, sm);
        });
    }

    protected T sumByColumn(int column) {
        T statistics = (T) new HashMap<K, Integer>();
        getRecords().forEach(record-> {
            String itemID = record[column];
            Integer sum = statistics.get(itemID);
            sum = (sum == null)? 1
                    : ++sum;
            statistics.put((K)itemID, sum);
        });

        return statistics;
    }

    @Override
    protected T getResult() {
        T result = (T) new TreeMap<K, Integer>((k1,k2)-> topItems.get(k2).compareTo(topItems.get(k1)));
        result.putAll(topItems);
        return result;
    }

    protected T getTopItems() {
        return topItems;
    }

    public GetTopItemsTask() {
        topItems = (T) new HashMap<K, Integer>();
    }
}
