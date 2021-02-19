package ru.vip.demo.entity;

import ru.vip.demo.type.Unit;

import java.math.BigDecimal;
import java.util.UUID;

public class Section extends Node { // Раздел работ
    private UUID id;

    private Unit unit;          // Еденица измерения
    private BigDecimal quantity;// Количество
    private BigDecimal price;   // Цена еденицы
}
