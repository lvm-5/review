package com.vlashchevskyi.review.pattern.task;

import com.csvreader.CsvReader;
import com.vlashchevskyi.review.pattern.ReviewTaskObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvm on 2/10/17.
 */
public class ReadReviewTask extends ReviewTaskObserver {
    private int limit = 1500;
    private final CsvReader reader;

    public ReadReviewTask(String pathToReview) throws IOException {
        reader = new CsvReader(pathToReview);
        reader.readHeaders();
    }

    @Override
    public Object doAction() throws Exception {
        List<String[]> records = read();
        subject.setRecords(records);
        return records;
    }

    public List<String[]> read() throws Exception {
        List<String[]> records = new ArrayList<>();
        for (int i = 0; reader.readRecord() && i < limit; i++) {
            records.add(reader.getValues());
        }

        return records;
    }

    @Override
    protected Object getResult() {
        return null;
    }


}
