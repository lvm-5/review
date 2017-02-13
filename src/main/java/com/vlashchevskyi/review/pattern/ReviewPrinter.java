package com.vlashchevskyi.review.pattern;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by lvm on 2/12/17.
 */
public class ReviewPrinter {
    public void printAllWith(List<Future> fes, int amount) throws ExecutionException, InterruptedException {
        final int ITEM_TASK = 1;
        final int USER_TASK = 2;
        final int WORD_TASK = 3;

        printWithStat(fes.get(USER_TASK), amount, "Top of the most active users");
        printWithStat(fes.get(ITEM_TASK), amount, "Top of the most commented products");
        printWithStat(fes.get(WORD_TASK), amount, "Top of the most used words");
    }

    public void printAll(List<Future> fes, int amount) throws ExecutionException, InterruptedException {
        final int ITEM_TASK = 1;
        final int USER_TASK = 2;
        final int WORD_TASK = 3;

        print(fes.get(USER_TASK), amount, "Top of the most active users");
        print(fes.get(ITEM_TASK), amount, "Top of the most commented products");
        print(fes.get(WORD_TASK), amount, "Top of the most used words");
    }

    <K, V, U extends Map.Entry<K, V>, T extends Stream<U>> void print(Future<T> fe, int amount, String subject) throws ExecutionException, InterruptedException {
        System.out.println(subject + "\n--");
        fe.get().limit(amount).map(e->e.getKey()).sorted().
                forEach(e -> System.out.println(e));
        System.out.println();

    }

    <K, V, U extends Map.Entry<K, V>, T extends Stream<U>> void printWithStat(Future<T> fe, int amount, String subject) throws ExecutionException, InterruptedException {
        System.out.println(subject + "\n--");
        fe.get().limit(amount).
                forEach(e -> System.out.println(e));
        System.out.println();

    }
}

