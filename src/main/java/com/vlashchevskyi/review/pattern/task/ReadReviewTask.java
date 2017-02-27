package com.vlashchevskyi.review.pattern.task;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvm on 2/10/17.
 */
public class ReadReviewTask<T extends List<String[]>> extends ReviewTaskObserver {
    private CsvReader reader;
    private T result;

    @Override
    public T doAction() throws Exception {
        mCalc.gc();
        T records = read();
        subject.setRecords(records);
        result = records;
        return records;
    }


    public T read() throws Exception {
        T records = (T) new ArrayList<String[]>();

        long capacity = mCalc.calcCapacity();
        while (records.size() < capacity
                && !emulator.isLimit(records)
                && reader.readRecord()) {
            records.add(reader.getValues());
        }
        return records;
    }

    @Override
    protected T getResult() {
        return result;
    }

    private void init(String pathToReview) throws IOException {
        reader = new CsvReader(pathToReview);
        reader.readHeaders();
    }

    public ReadReviewTask(String pathToReview) throws IOException {
        init(pathToReview);
    }


}
