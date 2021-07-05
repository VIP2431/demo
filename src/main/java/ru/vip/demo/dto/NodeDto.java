package ru.vip.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Status;
import ru.vip.demo.type.TypeItem;
import ru.vip.demo.type.Unit;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeDto implements Serializable {

    private int key;
    private int keyParent;
    private String codePoz;

    private UUID id;
    private TypeItem type; // Тип Блок/Позиция
    private Status status; // Статус
    private Unit unit;     // Еденица измерения
    private String name;   // Имя
    private String title;  // Title

    private int flags;
    private int i1;
    private int i2;
    private int i3;
    private double d1;
    private double d2;
    private double d3;
    private double d4;
}
