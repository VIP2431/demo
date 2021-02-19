package ru.vip.demo.entity;

import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.math.BigDecimal;
import java.util.UUID;

public class ItemDirectory { // Позиция материала\услуги в справочнике
    private UUID idItemDirectory;
    private Category category;  // Категория товара
    private String vendor;      // Код поставщика товара
    private String code;        // Код товара
    private String name;        // Название
    private Unit unit;          // Еденица измерения
    private BigDecimal price;   // Цена еденицы
}