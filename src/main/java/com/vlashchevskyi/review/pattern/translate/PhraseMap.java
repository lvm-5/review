package com.vlashchevskyi.review.pattern.translate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lvm on 2/24/17.
 */
public class PhraseMap {
    private final Map<Integer, LinkedList<String>> map;
    private boolean isKeysChanged = true;
    private List<Integer> keys = new ArrayList<>();

    public void put(String phrase) {
        Integer len = phrase.length() + BlockMaker.PHRASE_SPLITTER.length();
        LinkedList<String> pGroup = map.get(len);
        if (pGroup == null) {
            pGroup = new LinkedList();
            synchronized (this) {
                map.put(len, pGroup);
                turnKeysFgOn();
            }
        }
        pGroup.push(phrase);
    }

    public void putAll(Set<String> phrases) {
        phrases.forEach(p->put(p));
    }

    public String get(Integer len) {
        LinkedList<String> group = map.get(len);
        String phrase = null;
        if (group != null) {
            phrase = group.pollFirst();
            if (group.isEmpty()) {
                synchronized (this) {
                    map.remove(len);
                    turnKeysFgOn();
                }
            }
        }

        return phrase;
    }

    private synchronized void turnKeysFgOn() {
        if (!isKeysChanged) {
            isKeysChanged = true;
        }
    }

    public synchronized List<Integer> keyList() {
        if (isKeysChanged) {
            keys = map.keySet().stream().sorted((k1, k2) -> k2.compareTo(k1)).collect(Collectors.toList());
            isKeysChanged = false;
        }

        return keys;
    }

    public int many(Integer len) {
        List<String> phrases = map.get(len);
        return (phrases != null)? phrases.size(): 0;
    }

    public int size() {
        return map.size();
    }

    public PhraseMap(Set<String> phrases) {
        this();
        putAll(phrases);
    }

    public PhraseMap() {
        map = new HashMap<>();
    }
}
