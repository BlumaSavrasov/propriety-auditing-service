package com.example.cheggexercise.services;

import com.example.cheggexercise.db.CheggRepository;
import com.example.cheggexercise.model.User;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProprietyAuditingServiceImpl implements ProprietyAuditingService {

    private final CheggRepository myRepository;
    private Cache<String, ArrayList<Timestamp>> map;

    public ProprietyAuditingServiceImpl(CheggRepository myRepository){
        this.myRepository = myRepository;
        this.map =  CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public int getAmountOfRequestsInLastHour(String uid) {
        ArrayList<Timestamp> timestamps = map.getIfPresent(uid);
        Timestamp time = Timestamp.valueOf(LocalDateTime.now().minusHours(1));
        if(timestamps != null) {
            int index = binarySearch(timestamps, time);
            return timestamps.subList(index, timestamps.size() - 1).size();
        }
        return 0;
    }


    @Override
    public User insert(User userDao) {
        String uId = userDao.getUId();
        Timestamp time = userDao.getTimestamp();
        List<Timestamp> userTimestamps = map.getIfPresent(uId);
        if (userTimestamps != null) {
            userTimestamps.add(time);
        }
        else {
            ArrayList<Timestamp> timestamps = new ArrayList<>();
            timestamps.add(time);
            map.put(uId, timestamps);
        }
        return myRepository.save(userDao);
    }

    private int binarySearch(ArrayList<Timestamp> timestamps, Timestamp time) {
        return binarySearch(timestamps, 0, timestamps.size() - 1, time);
    }

    private int binarySearch(ArrayList<Timestamp> timestamps, int leftIndex, int rightIndex, Timestamp time) {
        if (rightIndex >= leftIndex) {
            int mid = leftIndex + (rightIndex - leftIndex) / 2;
            if (timestamps.get(mid) == time)
                return mid;
            if (timestamps.get(mid).after(time))
                return binarySearch(timestamps, leftIndex, mid - 1, time);
            return binarySearch(timestamps, mid + 1, rightIndex, time);
        }
        return 0;
    }
}
