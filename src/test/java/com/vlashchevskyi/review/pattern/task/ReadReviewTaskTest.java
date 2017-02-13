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
    public void testRead() throws Exception {
        ReadReviewTask task = getReadTask();
        assertNotNull(task);
        task.setLimit(100);
        assertEquals(LIMIT, task.read().size());
    }
}
