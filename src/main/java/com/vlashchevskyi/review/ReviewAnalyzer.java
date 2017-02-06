package com.vlashchevskyi.review;

import com.csvreader.CsvReader;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.vlashchevskyi.review.ReviewConstants.*;

public class ReviewAnalyzer {
    final private List<String[]> records = new ArrayList<>();

    // required to show first records at analyzing
    private int amount = 10;


    public ReviewAnalyzer() {
    }

    public void readRecords(String pathToReview) throws IOException {
        Map<String, String[]> uniqueRecords = new HashMap();
        CsvReader reader = new CsvReader(pathToReview);
        reader.readHeaders();

        for (int i = 0; reader.readRecord() && i < amount; i++) {
            String key = reader.get("ProductId") + reader.get("UserId");
            uniqueRecords.put(key, reader.getValues());
        }

        uniqueRecords.forEach((id, row) -> records.add(row));
    }


    public void analyze() {
        print(getActiveUsers(), "active users");
        print(getPopularItems(), "commented product IDs");
        print(getCommonWords(), "rated words");
    }


    public List<String> getActiveUsers() {
        final Map<String, String> activity = new TreeMap<>();
        records.forEach(row -> activity.put(row[TIME_COLUMN] + row[ID_COLUMN], row[PROFILE_COLUMN]));

        List<String> profiles = new ArrayList<>();
        activity.forEach((actTime, profile) -> profiles.add(profile));

        return profiles;
    }

    public List<String> getPopularItems() {
        final Map<String, Integer> statistics = calcByColumn(PRODUCT_ID_COLUMN);
        Map<String, Integer> items = new TreeMap<>(new ItemComparator(statistics));

        items.putAll(statistics);
        List<String> productIDs = new ArrayList<>(items.keySet());

        return productIDs.subList(0, fixAmount(productIDs, amount));
    }

    public List<String> getCommonWords() {
        final String pattern = "\\W|\\d";
        final Map<String, Integer> wordStatistics = new HashMap<>();

        for (String[] record : records) {
            String body = getReviewTextBody(record);
            String[] words = body.toLowerCase().split(pattern);
            countWords(wordStatistics, words);
        }

        final Map<String, Integer> sortedWordStatistics = new TreeMap<>(new ItemComparator(wordStatistics));
        sortedWordStatistics.putAll(wordStatistics);
        List<String> words = new ArrayList<>(sortedWordStatistics.keySet());

        return words.subList(0, fixAmount(words, amount));
    }

    private void countWords(Map<String, Integer> wordStatistics, String[] words) {
        for (String word : words) {
            if (!word.isEmpty()) {
                Integer sum = (wordStatistics.get(word));
                sum = (sum == null) ? 1 : ++sum;
                wordStatistics.put(word, sum);
            }
        }
    }

    private String getReviewTextBody(String[] record) {
        StringBuilder body = new StringBuilder(record[SUMMARY_COLUMN]).append(record[TEXT_COLUMN]);
        return body.toString();
    }

    private Map<String, Integer> calcByColumn(int column) {
        Map<String, Integer> statistics = new HashMap<>();

        for (String[] record : records) {
            String productId = record[column];
            Integer sum = statistics.get(productId);
            sum = (sum == null) ? 1 : ++sum;
            statistics.put(productId, sum);
        }

        return statistics;
    }


    private <T extends Collection> int fixAmount(T items, int amount) {
        return (items.size() < amount) ? items.size() : amount;
    }

    private List<String> filterRecords(List<String[]> records, int column) {
        List<String> filtered = records.stream().map(record -> record[column]).distinct().collect(Collectors.toList());

        return filtered;
    }

    private synchronized <T extends List<String>> void print(T records, String subject) {
        final String SPLITTER = "\n======================";
        final StringBuilder title = new StringBuilder().append(records.size()).append(" most ").append(subject).append(SPLITTER);

        System.out.println(title);
        records.sort((s, t1) -> s.compareToIgnoreCase(t1));
        records.forEach(record -> System.out.println(record + " "));
        System.out.println("\r");
    }

    public List<String[]> getRecords() {
        return records;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
