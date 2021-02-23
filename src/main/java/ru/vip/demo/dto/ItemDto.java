package ru.vip.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private UUID id;

    private BigDecimal quantity = BigDecimal.valueOf(1);    // Количество
    private BigDecimal cost = BigDecimal.valueOf(0);        // Стоимость

    private UUID idItemDirectory = null;
    private String vendor = null;                           // Код поставщика товара
    private Category category =  Category.NOT_CATEGORY;     // Категория товара
    private String code = null;                             // Код товара
    private String name = null;                             // Название
    private Unit unit = Unit.NOT_UNIT;                      // Еденица измерения
    private BigDecimal price = BigDecimal.valueOf(0);       // Цена еденицы

}
