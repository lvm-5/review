package com.vlashchevskyi.review.pattern.translate;

import org.junit.Test;
import tool.BaseTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vlashchevskyi.review.pattern.ReviewConstants.TEXT_COLUMN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/16/17.
 */
public class ReviewSplitterTest extends BaseTest {
    private ReviewSplitter splitter = new ReviewSplitter();


    @Test
    public void testBuildBlockBySeq() {
        List<String> phrases = new ArrayList();
        phrases.add("Hello");
        phrases.add("wow");
        phrases.add("Ok");

        List<String> block = splitter.buildBlockBySeq(phrases.iterator(),0, 10);
        assertEquals(3, block.size());
        assertEquals(10, block.stream().mapToInt(p-> p.length()).sum());
    }

    @Test
    public void testBuildBlock_Sequential() {
        List<String> phrases = preparePhrases();
        List<String> block = splitter.buildBlock(phrases, 10);
        assertEquals(3, block.size());
        assertEquals(10, block.stream().mapToInt(p-> p.length()).sum());
    }

    @Test
    public void testBuildBlock_Intermediate() {
        List<String> phrases = preparePhrases();
        List<String> block = splitter.buildBlock(phrases, 6);
        assertEquals(2, block.size());
        assertEquals(6, block.stream().mapToInt(p-> p.length()).sum());
    }

    @Test
    public void testBuildBlock_Part() {
        List<String> phrases = preparePhrases();
        List<String> block = splitter.buildBlock(phrases, 13);
        assertEquals(3, block.size());
        assertEquals(12, block.stream().mapToInt(p-> p.length()).sum());
    }


    private List<String> preparePhrases() {
        List<String> phrases = new ArrayList();
        phrases.add("Hello");
        phrases.add("Java");
        phrases.add("wow");
        phrases.add("Ok");

        return phrases;
    }

    @Test
    public void testSplit2Phrase_Review() throws Exception {
        List<String[]> reviews = task.read();
        long lim = 200;
        String text = (reviews.stream().filter(r -> r[TEXT_COLUMN].split(ReviewSplitter.SPLITTER).length > lim).collect(Collectors.toList()).get(0)[TEXT_COLUMN]);
        String[] phrases = text.split(ReviewSplitter.SPLITTER);
        Set<String> uniquePhrases = splitter.split2phrase(text);
        assertTrue(phrases.length > uniquePhrases.size());
    }

    @Test
    public void testSplit2Phrase() {
        String sentence1 = "Hello! How are you? Just think , ... about you";
        String sentence2 = "1 test; 2 test: n tests.";
        String sentence3 = "!Java is awesome!";
        String sentence4 = "!Java is awesome! ~Java is awesome~ ";

        isPhraseOk(sentence1, 4, "Just think");
        isPhraseOk(sentence2, 3, "2 test");
        isPhraseOk(sentence3, 1, "Java is awesome");
        isPhraseOk(sentence4, 1, "Java is awesome");

    }

    private void isPhraseOk(String sentence, int amount, String phrase)  {
        Set<String> res = splitter.split2phrase(sentence);
        System.out.println(res);
        assertEquals(amount, res.size());
        assertTrue(res.contains(phrase));
    }
}
