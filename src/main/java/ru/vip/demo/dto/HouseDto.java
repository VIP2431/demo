package ru.vip.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HouseDto extends NodeDto {
    private UUID id;

    private String address;     // Адресс объекта
    private String owner;       // Собственник
    private String email;       //
    private String telephone;   // Телефон собственника
    private String foreman;     // Прораб\Бригадир

}
