package tool;

import com.vlashchevskyi.review.pattern.task.ReadReviewTask;
import org.junit.Before;

import java.io.IOException;

/**
 * Created by lvm on 2/13/17.
 */
public class BaseTest {
    private ReadReviewTask readTask;
    public final int LIMIT = 100;

    @Before
    public void setBaseUp() {
        try {
            readTask = new ReadReviewTask("Reviews.csv" , LIMIT);
            readTask.setTestMode(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ReadReviewTask getReadTask() {
        return readTask;
    }

    public BaseTest() {

    }
}
