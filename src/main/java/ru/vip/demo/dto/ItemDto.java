package ru.vip.demo.dto;

import lombok.*;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {
    private UUID id;

    private BigDecimal quantity = BigDecimal.valueOf(1);// Количество
    private BigDecimal cost = BigDecimal.valueOf(0);    // Стоимость

    private UUID idItemDirectory;
    private Category category;                          // Категория товара
    private String code;                                // Код товара
    private String name;                                // Название
    private Unit unit;                                  // Еденица измерения
    private BigDecimal price;                           // Цена еденицы
    private String vendor;                              // Код поставщика товара
}
