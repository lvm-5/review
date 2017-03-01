package com.vlashchevskyi.review.pattern.translate;

import org.junit.Ignore;
import tool.BaseTest;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by lvm on 2/22/17.
 */
public class GoogleTranslateTest extends BaseTest{
    @Ignore
    public void testDoTranslate() throws Exception {
        //AppRunner.main(new String[]{});
        assertTrue(new GoogleTranslator().doTranslate(pathToReviews));
    }
}
