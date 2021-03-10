package ru.vip.demo.entity;

import lombok.Data;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
//@Table(name = "item_desc_t")
public class Item implements Serializable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private BigDecimal quantity = BigDecimal.valueOf(1);    // Количество
    private BigDecimal cost = BigDecimal.valueOf(0);        // Стоимость

    private UUID idItemDirectory = null;
    private String vendor = null;                           // Код поставщика товара
    private Category category =  Category.CTG_NOT;     // Категория товара
    private String code = null;                             // Код товара
    private String name = null;                             // Название
    private Unit unit = Unit.UN_NOT;                      // Еденица измерения
    private BigDecimal price = BigDecimal.valueOf(0);       // Цена еденицы
}
