package com.vlashchevskyi.review.pattern;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Stream;

/**
 * Created by lvm on 2/12/17.
 */
public class ReviewPrinter {

    public void printAll(Map<String, Future> result, int amount){
        result.forEach((subject, fe) -> {
            try {
                print(fe, amount, subject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

