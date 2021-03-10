package ru.vip.demo.entity;

import lombok.*;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class ItemDirectory implements Serializable {// Позиция материала\услуги в справочнике
    @Id
    @GeneratedValue //(strategy= GenerationType.AUTO)
    private UUID idItemDirectory;
    private Category category;                      // Категория товара
    private String code;                            // Код товара
    private String name;                            // Название
    private Unit unit;                              // Еденица измерения
    private BigDecimal price;                       // Цена еденицы
    private String vendor;                          // Код поставщика товара
}