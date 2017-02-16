package com.vlashchevskyi.review.pattern;

import com.vlashchevskyi.review.pattern.task.ReadReviewTask;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/13/17.
 */
public class RunnerTest {

    private Runner runner;
    private ReadReviewTask task;


    @Before
    public void setItUp() throws IOException {
        runner = new Runner();
        runner.setAMOUNT(10);
        task = new ReadReviewTask("Reviews.csv", 500);
        task.setTestMode(true);
    }

    @Test
    public void testTrigger() {
        try {
            runner.trigger(task);
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);
    }
}
