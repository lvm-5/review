package com.vlashchevskyi.review.pattern.translate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.BLOCK_SPLITTER;

/**
 * Created by lvm on 2/19/17.
 */
public class TranslateRequestTask<T extends Map<String, String>> implements Callable<T> {

    private final List<String> block;
    private final Translate service;

    @Override
    public T call() throws Exception {
        String message = prepareMessage(block);
        String translate = translate(message);
        return match(translate);
    }

    private String prepareMessage(List<String> block) {
        StringBuilder message = new StringBuilder();
        block.forEach(b -> message.append(b).append(BLOCK_SPLITTER));

        return message.toString();
    }

    private String translate(String message) {
        Translation translation = service.translate(message);
        return translation.getTranslatedText();
    }

    private T match(String translate) {
        T dictionary = (T)new HashMap<String, String>();
        String[] translates = translate.split(BLOCK_SPLITTER);

        for (int i = 0; i < block.size(); i++) {
            dictionary.put(block.get(i), translates[i]);
        }

        return dictionary;
    }

    public TranslateRequestTask(List<String> block, Translate service) {
        this.block = block;
        this.service = service;
    }
}
