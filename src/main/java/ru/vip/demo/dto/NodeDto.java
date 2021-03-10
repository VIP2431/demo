package ru.vip.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.entity.Item;
import ru.vip.demo.type.Status;
import ru.vip.demo.type.Unit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeDto implements Serializable {

    private UUID id;
    private String name;                                // Наименование узла
    private Status status;                              // Статус узла

    private Unit unit = Unit.UN_NOT;                  // Еденица измерения
    private BigDecimal quantity = BigDecimal.valueOf(0);// Количество
    private BigDecimal price = BigDecimal.valueOf(0);   // Цена еденицы

    private List<NodeDto> listNode;                     //Список Комнат/Разделов

    private List <Item> listItem;                       //Список Позиций

}
