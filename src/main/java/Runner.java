import com.vlashchevskyi.review.ReviewAnalyzer;

import java.io.IOException;

/**
 * Created by lvm on 2/6/17.
 */
public class Runner {
    private final Runtime rt = Runtime.getRuntime();
    private final int CPU_AMOUNT = rt.availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException {
        new Runner().trigger();
    }

    public void trigger() throws IOException {
        ReviewAnalyzer analyzer = new ReviewAnalyzer();
        analyzer.setAmount(10);
        analyzer.readRecords("Reviews.csv");
        analyzer.analyze();
    }
}
