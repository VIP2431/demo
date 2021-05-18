package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//@Transactional
@Builder
public class Node extends HeadingItem implements Serializable, Cloneable{

    @OneToMany ( cascade = CascadeType.ALL) //, fetch = FetchType.EAGER) //
    private List<Item> items;                           //Список Позиций

    @OneToMany ( cascade = CascadeType.ALL) //, fetch = FetchType.EAGER)
    private List<Node> nodes;                        //Список Комнат/Разделов

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        if (!super.equals(o)) return false;
        Node node = (Node) o;
        return Objects.equals(getItems(), node.getItems()) && Objects.equals(getNodes(), node.getNodes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getItems(), getNodes());
    }

    public Node clone() throws CloneNotSupportedException{ return (Node) super.clone(); }
}
