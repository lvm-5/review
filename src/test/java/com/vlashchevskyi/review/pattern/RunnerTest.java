package com.vlashchevskyi.review.pattern;

import org.junit.Before;
import org.junit.Test;
import tool.BaseTest;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/13/17.
 */
public class RunnerTest extends BaseTest {

    private Runner runner;

    @Before
    public void setItUp() throws IOException {
        runner = new Runner();
        runner.setAMOUNT(10);
    }

    @Test
    public void testTrigger() {
        try {
            runner.trigger(task);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        assertTrue(true);
    }
}
