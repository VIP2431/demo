package ru.vip.demo.entity;

import lombok.Data;
import ru.vip.demo.dto.NodeDto;
import ru.vip.demo.type.Unit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Section extends NodeDto {                     // Раздел работ
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    private Unit unit = Unit.NOT_UNIT;                  // Еденица измерения
    private BigDecimal quantity = BigDecimal.valueOf(0);// Количество
    private BigDecimal price = BigDecimal.valueOf(0);   // Цена еденицы
}