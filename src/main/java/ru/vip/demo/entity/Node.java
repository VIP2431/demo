package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Status;
import ru.vip.demo.type.Unit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Node implements Serializable {
    @Id
    @GeneratedValue //(strategy= GenerationType.AUTO)
    private UUID id;
    private String name;                                // Наименование узла
    private Status status;                              // Статус узла

    private Unit unit = Unit.UN_NOT;                    // Еденица измерения
    private BigDecimal quantity = BigDecimal.valueOf(0);// Количество
    private BigDecimal price = BigDecimal.valueOf(0);   // Цена еденицы

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id")
    private List<Node> listNode;                        //Список Комнат/Разделов

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private List<Item> listItem;                        //Список Позиций

}
