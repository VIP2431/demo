package ru.vip.demo.entity;

import java.util.UUID;

public class House extends Node {
    private UUID id;

    private String address;     // Адресс объекта
    private String owner;       // Собственник
    private String email;       //
    private String telephone;   // Телефон собственника
    private String foreman;     // Прораб\Бригадир
}
