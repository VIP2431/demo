package ru.vip.demo.dto;

import lombok.*;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDirectoryDto {
    private UUID idItemDirectory;
    @NonNull
    private Category category = Category.NOT_CATEGORY;  // Категория товара
    private String vendor = "Нет поставщика";           // Код поставщика товара
    private String code = "Нет кода";                   // Код товара
    private String name = "Нет наименования";           // Название
    @NonNull
    private Unit unit = Unit.NOT_UNIT;                  // Еденица измерения
    private BigDecimal price = BigDecimal.valueOf(1.25);// Цена еденицы

}
