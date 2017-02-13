package com.vlashchevskyi.review.pattern.task;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvm on 2/10/17.
 */
public class ReadReviewTask<T extends List<String[]>> extends ReviewTaskObserver {
    private int limit = 1500;
    private final CsvReader reader;
    private T result;

    @Override
    public T doAction() throws Exception {
        T records = (T)read();
        subject.setRecords(records);
        result = records;
        return records;
    }

    public T read() throws Exception {
        T records = (T)new ArrayList<String[]>();
        for (int i = 0; i < limit && reader.readRecord(); i++) {
            records.add(reader.getValues());
        }

        return records;
    }

    @Override
    protected T getResult() {
        return result;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public ReadReviewTask(String pathToReview, int limit) throws IOException {
        this(pathToReview);
        this.limit = limit;
    }

    public ReadReviewTask(String pathToReview) throws IOException {
        reader = new CsvReader(pathToReview);
        reader.readHeaders();
    }


}
