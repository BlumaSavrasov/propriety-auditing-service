package com.example.cheggexercise.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
public class UserEvent {
    @Id
    @GeneratedValue
    private Long id;
    private String uId;
    private String data;
    private Timestamp timestamp;

}
