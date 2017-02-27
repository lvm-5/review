package com.vlashchevskyi.review.pattern.translate;

import java.util.*;

/**
 * Created by lvm on 2/24/17.
 */
public class BlockMaker {
    private PhraseMap map;
    private Integer size;
    public static final String PHRASE_SPLITTER = ".";

    public List<List<String>> buildBlocks() {
        List blocks = new LinkedList();

        do {
            Map plan = planBlock(size);
            List block = buildBlock(plan);
            blocks.add(block);
        } while (map.size() > 0);

        return blocks;
    }

    public List<String> buildBlock(Map<Integer, Integer> plan) {
        List<String> block = new LinkedList<>();

        plan.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                block.add(map.get(k));
            }
        });

        return block;
    }

    public Map<Integer, Integer> planBlock(Integer target) {
        Map selected = new HashMap();

        for (int i = target; selected.isEmpty() && i > 0; i--) {
            selected = plan(i, 0);
        }

        return selected;
    }

    private Map<Integer, Integer> plan(Integer target, int position) {
        Map<Integer, Integer> selected = new HashMap<>();

        int pos = 0;
        List<Integer> keys = map.keyList();
        if (position >= keys.size() || (pos = findFitPos(keys, target, position)) >= keys.size()) {
            return selected;
        }

        int max = keys.get(pos);
        int requiredAmount = target / max;
        int actualAmount = map.many(max);

        int delta;
        if (actualAmount >= requiredAmount) {
            selected.put(max, requiredAmount);
            delta = target - max * requiredAmount;
        } else {
            delta = target - max * actualAmount;
        }

        if (delta > 0) {
            Map inner = new HashMap();
            for (int j = pos + 1; (inner.isEmpty()) && j < keys.size(); j++) {
                inner = plan(delta, j);
            }

            if (!inner.isEmpty()) {
                selected.putAll(inner);
                selected.put(max, actualAmount);
            } else {
                selected = plan(target, pos + 1);
            }
        }

        return selected;
    }

    private int findFitPos(List<Integer> keys, int target, int position) {
        int length = keys.get(position);
        if (length == target || length < target) {
            return position;
        }

        int start = position;
        int end = keys.size();
        int half = (end - start) / 2;
        int cur;
        do {
            cur = start + half;
            int len = keys.get(cur);
            if (len > target) {
                start = cur;
            } else if (len < target) {
                end = cur;
            } else if (len == target) {
                break;
            }
            half =  (end - start) / 2;
        } while (half > 0);
        if (half == 0) {
            cur = (keys.get(start) < target)? start : end;
        }

        return cur;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public void setMap(PhraseMap map) {
        this.map = map;
    }

    public PhraseMap getMap() {
        return map;
    }

    public BlockMaker(PhraseMap map) {
        this.map = map;
        this.size = 1000;
    }

    public BlockMaker(PhraseMap map, Integer size) {
        this.map = map;
        this.size = size;
    }
}
