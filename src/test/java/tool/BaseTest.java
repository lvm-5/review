package tool;

import com.vlashchevskyi.review.pattern.task.ReadReviewTask;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

import static com.vlashchevskyi.review.pattern.test.Emulator.emulator;

/**
 * Created by lvm on 2/18/17.
 */
public class BaseTest {
    protected ReadReviewTask task;
    private static final int RECORDS_AMOUNT = 100;

    public BaseTest() {

    }

    @BeforeClass
    public static void baseClassSetUp() {
        if (!emulator.getTestMode()) {
            emulator.setTestMode(true);
            emulator.setLimit(RECORDS_AMOUNT);

        }
    }

    @Before
    public void baseSetUp() {
        try {
            task = new ReadReviewTask("Reviews.csv", 500);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
