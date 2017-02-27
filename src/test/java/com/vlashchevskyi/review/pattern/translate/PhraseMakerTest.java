package com.vlashchevskyi.review.pattern.translate;

import org.junit.Test;
import tool.BaseTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.vlashchevskyi.review.pattern.ReviewConstants.TEXT_COLUMN;
import static com.vlashchevskyi.review.pattern.translate.SplitterConstants.REVIEW_SPLITTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/16/17.
 */
public class PhraseMakerTest extends BaseTest {
    private static final int BLOCK_SIZE = 1000;
    private static final int PHRASE_AMOUNT = 100;

    private PhraseMaker splitter = new PhraseMaker(BLOCK_SIZE);

    @Test
    public void testDoPhraseByPos() {
        String phrase = "recived my new triple gum ball machine  today fast delivery service for the price n weight the fourty bucks was worth it it came unbroken opening the box all threre machines were intact so i figured it would be a easy set up was i ever wrong you have to practicly take all three machines apart to install them and soon as you loosen the locks the whole machine falls apart as you lift the plastic glob off i am kind of not looking forward to filling them meaning i will have to unlock the lids again so the whole thing will fall apart again it took me half the morning to put it together and i am a machinest the screws are miss matched also and should be told before hand about all the assmbly required and adjustments to the interier wheels setting to allow products to go thru the inner gear wheels in other words how much product is dispenced can be adjusted but i would have liked to have known before hand how much assembly was required and what tools would be needed  figure on spending 4 hours assembly and adjustments to dispencing wheels now that it is all set up it reminds me of when i was young";
        List<String> phrases = splitter.doPhraseByPos(phrase);
        assertTrue(phrases.size() > 1);
        phrases.forEach(p->assertTrue(p.length() <= BLOCK_SIZE));
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
    public void testDoPhraseByPattern_Review() throws Exception {
        List<String[]> reviews = task.read();
        String text = (reviews.stream().filter(r -> r[TEXT_COLUMN].split(REVIEW_SPLITTER).length > PHRASE_AMOUNT).collect(Collectors.toList()).get(0)[TEXT_COLUMN]);
        String[] phrases = text.split(REVIEW_SPLITTER);
        Set<String> uniquePhrases = splitter.doPhraseByPattern(text);

        assertTrue(phrases.length > uniquePhrases.size());
    }

    @Test
    public void testDoPhraseByPattern() {
        String marker = "about you";
        String sentence1 = "Hello! How are you? Just think , ... " + marker;
        Set<String> phrases = splitter.doPhraseByPattern(sentence1);
        assertEquals(marker, phrases.iterator().next());
        isPhraseOk(sentence1, 4, "Just think");

        String sentence2 = "1 test; 2 test: n tests.";
        isPhraseOk(sentence2, 3, "2 test");

        String sentence3 = "!Java is awesome!";
        isPhraseOk(sentence3, 1, "Java is awesome");

        String sentence4 = "!Java is awesome! ~Java is awesome~ ";
        isPhraseOk(sentence4, 1, "Java is awesome");

    }

    private void isPhraseOk(String sentence, int phraseAmount, String phrase)  {
        Set<String> res = splitter.doPhraseByPattern(sentence);
        System.out.println(res);

        assertEquals(phraseAmount, res.size());
        assertTrue(res.contains(phrase));
    }

    @Test
    public void testGatherSplitters() {
        String review = "hi John, how are you?";
        List<String> splitters = splitter.gatherSplitters(review);
        splitters.forEach(s-> {
            System.out.println(s);
        });
        assertEquals(Arrays.asList(new String[]{",", "?"}), splitters);
    }

}
