package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
//@Table(name = "item_directory_t")
public class ItemDirectory implements Serializable {// Позиция материала\услуги в справочнике
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID idItemDirectory;
    private Category category;                      // Категория товара
    private String code;                            // Код товара
    private String title;                           // Наименование
    private Unit unit;                              // Еденица измерения
    private BigDecimal price;                       // Цена еденицы
    private String vendor;                          // Код поставщика товара
}