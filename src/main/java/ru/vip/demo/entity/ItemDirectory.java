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
public class ItemDirectory implements Serializable {             // Позиция материала\услуги в справочнике
    @Id
    @GeneratedValue //(strategy= GenerationType.AUTO)
    private UUID idItemDirectory;
    private Category category = Category.NOT_CATEGORY;   // Категория товара
    private String vendor = "Нет поставщика1";           // Код поставщика товара
    private String code = "Нет кода1";                   // Код товара
    private String name = "Нет наименования1";           // Название
    private Unit unit = Unit.NOT_UNIT;                   // Еденица измерения
    private BigDecimal price = BigDecimal.valueOf(1.10); // Цена еденицы
}