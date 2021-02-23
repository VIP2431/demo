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
public class House extends NodeDto {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private String address;     // Адресс объекта
    private String owner;       // Собственник
    private String email;       //
    private String telephone;   // Телефон собственника
    private String foreman;     // Прораб\Бригадир
}
