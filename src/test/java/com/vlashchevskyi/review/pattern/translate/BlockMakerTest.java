package com.vlashchevskyi.review.pattern.translate;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by lvm on 2/24/17.
 */
public class BlockMakerTest {
    private BlockMaker bMaker;

    @Before
    public void setUp() {
        bMaker = null;
    }

    private PhraseMap preparePhrases() {
        PhraseMap map = new PhraseMap();

        map.put("Hello");
        map.put("Java");
        map.put("wow");
        map.put("Ok");

        return map;
    }

    @Test
    public void testBuildBlock() {
        PhraseMap map = preparePhrases();
        bMaker = new BlockMaker();
        bMaker.setMap(map);

        int size = 18;
        Map plan = bMaker.planBlock(size);
        List<String> block = bMaker.buildBlock(plan);
        assertEquals(0, map.size());
        assertEquals(14, block.stream().mapToInt(p->p.length()).sum());
    }

    @Test
    public void testPlanBlock_Part() {
        PhraseMap map = preparePhrases();
        bMaker = new BlockMaker();
        bMaker.setMap(map);

        int size = 16;
        Map<Integer, Integer> plan = bMaker.planBlock(size);

        int amount = 3;
        assertEquals(amount, plan.size());
        assertEquals(15, plan.keySet().stream().mapToInt(l->l).sum());

    }

    @Test
    public void testPlanBlock_Intermediate() {
        PhraseMap map = preparePhrases();
        bMaker = new BlockMaker();
        bMaker.setMap(map);

        int size = 8;
        Map<Integer, Integer> plan = bMaker.planBlock(size);

        int amount = 2;
        assertEquals(amount, plan.size());
        assertEquals(size, plan.keySet().stream().mapToInt(l->l).sum());
    }

    @Test
    public void testPlanBlock() {
        String marker = "Ok";

        PhraseMap map = new PhraseMap();
        map.put("Hello");
        map.put("wow");
        map.put(marker);

        bMaker = new BlockMaker();
        bMaker.setMap(map);

        Integer size = 13;
        Map<Integer, Integer> plan = bMaker.planBlock(size);
        assertEquals(3, plan.size());
        assertEquals(marker, map.get(marker.length() + BlockMaker.PHRASE_SPLITTER.length()));
    }

}
