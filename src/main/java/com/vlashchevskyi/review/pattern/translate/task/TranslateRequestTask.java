package com.vlashchevskyi.review.pattern.translate.task;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lvm on 2/19/17.
 */
public class TranslateRequestTask<T extends Map<String, String>
        , U extends LinkedList<List<String>>> extends TranslateTaskObserver<T, U> {

    private final Translate.TranslateOption srcLang;
    private final Translate.TranslateOption trgtLang;
    private final Translate service;
    private final T dictionary;

    private static final Logger logger = Logger.getLogger("com.vlashchevskyi.review.pattern.translate");

    @Override
    public T doAction() throws Exception {
        List<String> block = getBlock();
        if (block == null) {
            return (T) new HashMap<String, String>();
        }

        T dic = translate(block);
        aggregate(dic);

        return dic;
    }

    private T translate(List<String> blocks) {
        T dictionary = (T) new HashMap<String, String>();
        List<Translation> translation = service.translate(blocks, srcLang, trgtLang);

        if (translation.size() == blocks.size()) {
            for (int i = 0; i < blocks.size(); i++) {
                dictionary.put(blocks.get(i), translation.get(i).getTranslatedText());
            }
        } else if (!emulator.getTestMode()) {
            synchronized (this) {
                logger.warn("translation isn't equaled to source");
                StringBuilder srcText = new StringBuilder();
                blocks.forEach(b -> srcText.append(b).append("."));
                logger.warn("source text: " + srcText.toString());
            }
        }

        return dictionary;
    }

    private List<String> getBlock() {
        U blocks = getResource();
        return blocks.pollFirst();
    }

    @Override
    protected T getResult() {
        return dictionary;
    }

    private void aggregate(T mapping) {
        dictionary.putAll(mapping);
    }

    public TranslateRequestTask(Translate service, Translate.TranslateOption srcLang, Translate.TranslateOption trgtLang) {
        this.service = service;
        this.srcLang = srcLang;
        this.trgtLang = trgtLang;
        this.dictionary = (T) new HashMap<String, String>();
    }
}
