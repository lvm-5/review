package com.vlashchevskyi.review.pattern.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vlashchevskyi.review.pattern.ReviewConstants.PROFILE_COLUMN;
import static com.vlashchevskyi.review.pattern.ReviewConstants.USER_ID_COLUMN;

/**
 * Created by lvm on 2/10/17.
 */
public class GetTopUsersTask<T extends Map<User, Integer>, U extends List<String[]>> extends GetTopItemsTask<User, T, U> {

    @Override
    public synchronized T analyze() throws Exception {
        T mp = sumByColumn(USER_ID_COLUMN);
        mp.forEach((ur, sum) -> {
            Integer sm = getTopItems().get(ur);
            getTopItems().put(ur, sm != null ? sm + sum : sum);
        });
        return mp;
    }

    @Override
    protected T sumByColumn(int column) {
        T statistics = (T) new HashMap<User, Integer>();
        getResource().forEach(record -> {
            User ur = new User(record[column], record[PROFILE_COLUMN]);
            Integer sum = statistics.get(ur);
            sum = (sum == null) ? 1 : ++sum;
            statistics.put(ur, sum);
        });

        return statistics;
    }
}

final class User implements Comparable<User> {
    private final String id;
    private final String profile;

    public User(String id, String profile) {
        this.id = id;
        this.profile = profile;
    }

    @Override
    public boolean equals(Object ur) {
        boolean stat = false;

        if (ur instanceof User) {
            User u = (User) ur;
            stat = id.compareTo(u.getId()) == 0;
        }

        return stat;
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public String toString() {
        return profile;
    }

    @Override
    public int compareTo(User ur) {
        return profile.compareToIgnoreCase(ur.getProfile());
    }
}
