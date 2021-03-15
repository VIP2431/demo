package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Table(name = "item_t")
public class Item implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String name;            // Имя
    private BigDecimal quantity;    // Количество
    private BigDecimal cost;        // Стоимость

    private UUID idItemDirectory;
    private Category category;      // Категория товара
    private String code;            // Код товара
    private String title;           // Наименование
    private Unit unit;              // Еденица измерения
    private BigDecimal price;       // Цена еденицы
    private String vendor;          // Код поставщика товара

}
