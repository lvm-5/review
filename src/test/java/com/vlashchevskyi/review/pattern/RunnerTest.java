package com.vlashchevskyi.review.pattern;

import org.junit.Before;
import org.junit.Ignore;
import tool.BaseTest;

import static org.junit.Assert.assertTrue;

/**
 * Created by lvm on 2/13/17.
 */
public class RunnerTest extends BaseTest {

    private Runner runner;

    @Before
    public void setUp() {
        runner = new Runner();
    }

    @Ignore
    public void testTrigger() {
        try {
            runner.trigger(getReadTask());
        } catch (Exception e) {
            assertTrue(false);
        }

        assertTrue(true);
    }
}
