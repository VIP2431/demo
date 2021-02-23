package ru.vip.demo.entity;

import lombok.Data;
import ru.vip.demo.dto.NodeDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Room extends NodeDto {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

}
