package com.example.cheggexercise.db;

import com.example.cheggexercise.model.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface CheggRepository extends JpaRepository<UserEvent,String> {
    List<Timestamp> findByuIdAndTimestampGreaterThan(String uId,Timestamp timestamp);
}
