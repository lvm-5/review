package com.vlashchevskyi.review.pattern.translate;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/24/17.
 */
public class PhraseMapTest {
    private PhraseMap phrases;
    private String[] marker = new String[]{"Hello", "salut"};

    @Before
    public void setUp() {
        phrases = new PhraseMap();
    }

    @Test
    public void testPut() {
        Integer len = 6;

        phrases.put(marker[0]);
        assertEquals(1, phrases.size());
        assertEquals(marker[0], phrases.get(len));

        phrases.put(marker[1]);
        assertEquals(1, phrases.size());
        assertEquals(marker[1], phrases.get(len));
        assertEquals(0, phrases.size());
    }

    @Test
    public void sort() {
        String[] markers = new String[]{"1", "two", "three"};
        for (int i = 0; i < markers.length; i++) {
            phrases.put(markers[i]);
        }

        Iterator<Integer> it = phrases.keyList().iterator();
        assertTrue(it.next().compareTo(it.next()) > 0);
    }

}
