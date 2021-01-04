package ru.vip.demo.entity;


import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.TimeZone;

@Data
@Entity
public class Person {
    @Id
    Long id;
    @NonNull
    String name;
    @DateTimeFormat
    TimeZone date;
}
