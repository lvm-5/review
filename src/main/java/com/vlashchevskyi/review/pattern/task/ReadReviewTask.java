package com.vlashchevskyi.review.pattern.task;

import com.csvreader.CsvReader;
import com.vlashchevskyi.review.pattern.MemoryCalc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvm on 2/10/17.
 */
public class ReadReviewTask<T extends List<String[]>> extends ReviewTaskObserver {
    private CsvReader reader;
    private T result;
    private MemoryCalc mCalc;

    @Override
    public T doAction() throws Exception {
        mCalc.freeMem();
        T records = read();
        subject.setRecords(records);
        result = records;
        return records;
    }


    public T read() throws Exception {
        T records = (T) new ArrayList<String[]>();
        while (mCalc.isFreeMemory() && reader.readRecord() && emulator.doToLimit(records)) {
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
    public ReadReviewTask(String pathToReview, long limitInMb) throws IOException {
        init(pathToReview);
        mCalc = new MemoryCalc(limitInMb);
    }

    public ReadReviewTask(String pathToReview) throws IOException {
        init(pathToReview);
        mCalc = new MemoryCalc(500);
    }


}
