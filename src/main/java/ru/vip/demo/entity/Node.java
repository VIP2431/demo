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
//@Table(name = "node_t")
public class Node implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String name;                                // Имя блока
    private String title;                               // Наименование
    private Status status;                              // Статус узла

    private Unit unit = Unit.UN_NOT;                    // Еденица измерения
    private BigDecimal quantity = BigDecimal.valueOf(0);// Количество
    private BigDecimal price = BigDecimal.valueOf(0);   // Цена еденицы

    @OneToMany (cascade = CascadeType.ALL)  //, fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id")
    private List<Item> items;                        //Список Позиций

//    @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany (cascade = CascadeType.ALL) //, fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id")
    private List<Node> nodes;                        //Список Комнат/Разделов

    public Node clone() throws CloneNotSupportedException{ return (Node) super.clone(); }
}
