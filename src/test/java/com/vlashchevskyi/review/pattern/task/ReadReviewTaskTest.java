package com.vlashchevskyi.review.pattern.task;

import org.junit.Test;
import tool.BaseTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by lvm on 2/13/17.
 */
public class ReadReviewTaskTest extends BaseTest{
    @Test
    public  void readTask() throws Exception {
        assertNotNull(task);
        assertEquals(ReadReviewTask.emulator.getLimit(), task.read().size());
    }
}
