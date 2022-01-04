package com.example.cheggexercise.services;

import com.example.cheggexercise.db.CheggRepository;
import com.example.cheggexercise.model.UserEvent;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProprietyAuditingServiceImpl implements ProprietyAuditingService {

    private final CheggRepository cheggRepository;
    private Cache<String, List<Timestamp>> uId2Timestamps;

    public ProprietyAuditingServiceImpl(CheggRepository cheggRepository){
        this.cheggRepository = cheggRepository;
        initCache();
    }

    private void initCache() {
        this.uId2Timestamps =  CacheBuilder.newBuilder().build();
        /*We can have 3 modes that can easily be configured via application.properties for example.
        * We can have 3 mode:
        * Mode 1 - load all the users from the db that hava events from the last hour.
        * Mode 2 - load only the most active user from the db that have events from the last hour.(Ids of the users can be specified in external file)
        * Mode 3 - load an empty cache (this is my implementation)
        * */
    }

    @Override
    @SneakyThrows
    public int getAmountOfRequestsInLastHour(String uid) {
        Timestamp time = Timestamp.valueOf(LocalDateTime.now().minusHours(1));
        List<Timestamp> timestamps = uId2Timestamps.get(uid,() -> cheggRepository.findByuIdAndTimestampGreaterThan(uid,time));

        if(timestamps != null && !timestamps.isEmpty()) {
            if(timestamps.get(timestamps.size()).before(time)){
                uId2Timestamps.put(uid,new ArrayList<>());
                return 0;
            }
            int index = binarySearch(timestamps, time);
            List<Timestamp> relevantTimestamps = timestamps.subList(index, timestamps.size());
            uId2Timestamps.put(uid,relevantTimestamps);
            return relevantTimestamps.size();
        }
        return 0;
    }


    @Override
    public UserEvent insert(UserEvent userEventDao) {
        String uId = userEventDao.getUId();
        Timestamp time = userEventDao.getTimestamp();
        List<Timestamp> userTimestamps = uId2Timestamps.getIfPresent(uId);
        if (userTimestamps != null) {
            userTimestamps.add(time);
        }
        else {
            List<Timestamp> timestamps = new ArrayList<>();
            timestamps.add(time);
            uId2Timestamps.put(uId, timestamps);
        }
        return cheggRepository.save(userEventDao);
    }

    private int binarySearch(List<Timestamp> timestamps, Timestamp time)  {
        int start = 0;
        int end = timestamps.size() -1;

        for (int i = 0; i < timestamps.size(); i++)   {
            int middle = (end - start)/2;
            if (timestamps.get(i) == time)  {
                return i;
            }
            else if (timestamps.get(middle).after(time))  {
                end = middle - 1;
            }
            else {
                start = middle + 1;
            }
        }
        return 0;
    }

    /*I implemented the binary search with a for loop and not
    * with a recursion because in case will have millions of users
    * we will have stuck overflow. But if you think that the amount
    * of users is not that big, and you like recursion - here is a second
    * implementation for the binary search and with recursion this time.

    private int binarySearch(List<Timestamp> timestamps, Timestamp time) {
        return binarySearch(timestamps, 0, timestamps.size() - 1, time);
    }

    private int binarySearch(List<Timestamp> timestamps, int leftIndex, int rightIndex, Timestamp time) {
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
    * */


}
