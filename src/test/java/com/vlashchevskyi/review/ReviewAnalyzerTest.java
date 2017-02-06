package com.vlashchevskyi.review;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

;

/**
 * Created by lvm on 2/5/17.
 */
public class ReviewAnalyzerTest {
    private static ReviewAnalyzer analyzer;

    @BeforeClass
    public static void setUp() {
        try {
            analyzer = new ReviewAnalyzer();
            analyzer.readRecords("Reviews.csv");
            analyzer.setAmount(10);
        } catch (IOException e) {
            assertTrue(false);
        }
    }

    @Test
    public void getRecordsTest() {
        estimateRecords();
    }

    public void estimateRecords() {
        List<String[]> records = analyzer.getRecords();
        assertTrue(analyzer.getAmount() > 0);
        assertEquals(records.size(), analyzer.getAmount());
        assertFalse(records.get(0).equals(records.get(1)));
    }

}
