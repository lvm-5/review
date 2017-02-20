package com.vlashchevskyi.review.pattern;

import org.junit.Before;
import org.junit.Test;
import tool.BaseTest;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by lvm on 2/13/17.
 */
public class RunnerTest extends BaseTest {

    private Runner runner;

    @Before
    public void setItUp() throws IOException {
        runner = new Runner("Reviews.csv");
        runner.setAMOUNT(10);
    }

    @Test
    public void testTrigger() {
        assertTrue(runner.handle(false));
    }

    @Test
    public void testTrigger_Translate() {
        assertTrue(runner.handle(true));
    }
}
