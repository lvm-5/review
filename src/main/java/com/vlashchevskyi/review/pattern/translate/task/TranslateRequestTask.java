package com.vlashchevskyi.review.pattern.translate.task;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import com.vlashchevskyi.review.pattern.task.ReviewTaskObserver;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.BLOCK_SPLITTER;

/**
 * Created by lvm on 2/19/17.
 */
public class TranslateRequestTask<T extends Map<String, String>
        , U extends LinkedList<List<String>>> extends ReviewTaskObserver<T, U> {

    private final Translate.TranslateOption srcLang;
    private final Translate.TranslateOption trgtLang;
    private final Translate service;
    private final T dictionary;

    private static final Logger logger = Logger.getLogger("com.vlashchevskyi.review.pattern.translate");

    @Override
    public T doAction() throws Exception {
        List<String> block = getBlock();
        if (block == null) {
            return (T)new HashMap<String, String>();
        }

        String message = prepareMessage(block);
        String translate = translate(message);
        T dic = match(translate, block);
        aggregate(dic);

        return dic;
    }

    private List<String> getBlock() {
        return getResource().pollFirst();
    }

    @Override
    protected T getResult() {
        return dictionary;
    }

    private void aggregate(T mapping) {
        dictionary.putAll(mapping);
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

    private T match(String translate, List<String> block) {
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

    public TranslateRequestTask(Translate service, Translate.TranslateOption srcLang, Translate.TranslateOption trgtLang) {
        this.service = service;
        this.srcLang = srcLang;
        this.trgtLang = trgtLang;
        this.dictionary = (T) new HashMap<String, String>();
    }
}
