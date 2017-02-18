package com.vlashchevskyi.review.pattern.translate;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lvm on 2/16/17.
 */
public class GoogleTranslator {
    List<String> reviews = new ArrayList<>();
    List<String> translates = new ArrayList<>();
    private int connectionLimit = 100;
    private int counter;

    public void doTranslate() {
        counter = 0;
        reviews.parallelStream().forEach(review->{
            //translates.add(clt.translate);
        });
    }


}
