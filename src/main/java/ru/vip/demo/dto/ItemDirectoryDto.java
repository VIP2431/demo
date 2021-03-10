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
public class ItemDirectoryDto implements Serializable {
    private UUID idItemDirectory;
    @NonNull
    private Category category = Category.CTG_NOT;       // Категория товара
    private String code = "CODE_NOT";                   // Код товара
    private String name = "Пустая позиция";             // Название
    private Unit unit = Unit.UN_NOT;                    // Еденица измерения
    private BigDecimal price = BigDecimal.valueOf(0);   // Цена еденицы
    private String vendor = "Поставщика нет";           // Код поставщика товара

}
