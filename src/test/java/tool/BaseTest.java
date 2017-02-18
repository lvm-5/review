package tool;

import com.vlashchevskyi.review.pattern.task.ReadReviewTask;
import com.vlashchevskyi.review.pattern.task.ReviewTask;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;

/**
 * Created by lvm on 2/18/17.
 */
public class BaseTest {
    protected ReadReviewTask task;

    public BaseTest() {

    }

    @BeforeClass
    public static void baseClassSetUp() {
        if (!ReviewTask.emulator.getTestMode()) {
            ReviewTask.emulator.setTestMode(true);
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
