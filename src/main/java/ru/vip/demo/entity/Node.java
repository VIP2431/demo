package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.type.Status;
import ru.vip.demo.type.Unit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Transactional
@Builder
//@Table(name = "node_t")
public class Node extends HeadingItem implements Serializable, Cloneable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String name;                                // Имя блока
    private String title;                               // Наименование
    private Status status;                              // Статус узла

    private Unit unit = Unit.UN_NOT;                    // Еденица измерения
    private BigDecimal quantity = BigDecimal.valueOf(0);// Количество
    private BigDecimal price = BigDecimal.valueOf(0);   // Цена еденицы

    @OneToMany ( cascade = CascadeType.ALL) //, fetch = FetchType.EAGER) //
 //   @JoinColumn(name = "item_id")
    private List<Item> items;                           //Список Позиций

  //  @SuppressWarnings("JpaDataSourceORMInspection")
    @OneToMany ( cascade = CascadeType.ALL) //, fetch = FetchType.EAGER)
    //  @JoinColumn(name = "node_id")
    private List<Node> nodes;                        //Список Комнат/Разделов

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!super.equals(o)) return false;
        Node node = (Node) o;
        return getId().equals(node.getId()) && getName().equals(node.getName()) && getTitle().equals(node.getTitle()) && getStatus() == node.getStatus() && getUnit() == node.getUnit() && Objects.equals(getQuantity(), node.getQuantity()) && Objects.equals(getPrice(), node.getPrice()) && Objects.equals(getItems(), node.getItems()) && Objects.equals(getNodes(), node.getNodes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getName(), getTitle(), getStatus(), getUnit(), getQuantity(), getPrice(), getItems(), getNodes());
    }

    public Node clone() throws CloneNotSupportedException{ return (Node) super.clone(); }
}
