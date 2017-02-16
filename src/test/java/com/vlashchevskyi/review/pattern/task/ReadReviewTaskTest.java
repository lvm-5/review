package com.vlashchevskyi.review.pattern.task;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/13/17.
 */
public class ReadReviewTaskTest {
    private ReadReviewTask task;

    @Before
    public void setUp() throws IOException {
        task = new ReadReviewTask("Reviews.csv", 500);
        task.setTestMode(true);
    }
    @Test
    public void testRead() throws Exception {
        assertNotNull(task);
        assertTrue(task.read().size() > 0);
    }
}
