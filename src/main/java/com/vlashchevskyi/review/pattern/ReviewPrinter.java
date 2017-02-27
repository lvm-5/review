package com.vlashchevskyi.review.pattern;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

     <K extends Comparable, V, T extends Map<K, V>> void print(Future<T> fe, int amount, String subject) throws ExecutionException, InterruptedException {
        System.out.println(subject + "\n--");
        fe.get().entrySet()
                .stream()
                .limit(amount)
                .sorted((e1, e2)-> e1.getKey().compareTo(e2.getKey()))
                .forEach((e) -> System.out.println(e.getKey()));
        System.out.println();

    }
}

