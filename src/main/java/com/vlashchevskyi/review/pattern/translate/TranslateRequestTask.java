package com.vlashchevskyi.review.pattern.translate;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import com.vlashchevskyi.review.pattern.task.ReviewTask;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.BLOCK_SPLITTER;

/**
 * Created by lvm on 2/19/17.
 */
public class TranslateRequestTask<T extends Map<String, String>> implements ReviewTask<T> {

    private final Translate.TranslateOption srcLang;
    private final Translate.TranslateOption trgtLang;
    private final List<String> block;
    private final Translate service;

    private static final Logger logger = Logger.getLogger("com.vlashchevskyi.review.pattern.translate");

    @Override
    public T call() throws Exception {
        return doAction();
    }

    @Override
    public T doAction() throws Exception {
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

        Translation translation = service.translate(message, srcLang, trgtLang);
        return translation.getTranslatedText();
    }

    private T match(String translate) {
        T dictionary = (T)new HashMap<String, String>();
        String[] translates = translate.split(BLOCK_SPLITTER);

        if (translates.length == block.size()) {
            for (int i = 0; i < block.size(); i++) {
                dictionary.put(block.get(i), translates[i]);
            }
        } else {
            if (emulator.getTestMode()) {
                logger.warn("translation isn't equaled to source");
            }
        }

        return dictionary;
    }

    public TranslateRequestTask(List<String> block, Translate service, Translate.TranslateOption srcLang, Translate.TranslateOption trgtLang) {
        this.block = block;
        this.service = service;
        this.srcLang = srcLang;
        this.trgtLang = trgtLang;
    }
}
