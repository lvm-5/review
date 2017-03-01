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

    @Before
    public void setUp() {
        phrases = new PhraseMap();
    }

    @Test
    public void testPut() {
        String phrase = "Hello";
        phrases.put(phrase);
        assertEquals(1, phrases.size());

        Integer len = phrase.length();
        assertEquals(phrase, phrases.get(len));
    }

    @Test
    public void testPut_SpecialSymbol() {
        String phrase = "/Hello";
        phrases.put(phrase);
        assertEquals(1, phrases.size());

        int len = phrase.length() + new String("/").length();
        assertEquals("\\/Hello", phrases.get(len));
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
