package ru.vip.demo.entity;

import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.math.BigDecimal;
import java.util.UUID;

public class Item {
    private UUID id;
    private BigDecimal quantity;// Количество
    private BigDecimal cost;    // Стоимость

    private UUID idItemDirectory;
    private String vendor;      // Код поставщика товара
    private Category category;  // Категория товара
    private String code;        // Код товара
    private String name;        // Название
    private Unit unit;          // Еденица измерения
    private BigDecimal price;   // Цена еденицы
}
