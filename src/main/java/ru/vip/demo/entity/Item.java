package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Category;
import ru.vip.demo.type.Unit;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
// @Table(name = "item_t")
public class Item extends HeadingItem implements Serializable, Cloneable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
 //   @Column(name = "item_id")
    private UUID id;
    private String name;            // Имя
    private BigDecimal quantity;    // Количество
    private BigDecimal cost;        // Стоимость

    private UUID idItemDirectory;
    private Category category;      // Категория товара
    private String code;            // Код товара
    private String title;           // Наименование

    private Unit unit;              // Еденица измерения
    private BigDecimal price;       // Цена еденицы
    private String vendor;          // Код поставщика товара

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return Objects.equals(getId(), item.getId()) && Objects.equals(getName(), item.getName()) && Objects.equals(getQuantity(), item.getQuantity()) && Objects.equals(getCost(), item.getCost()) && Objects.equals(getIdItemDirectory(), item.getIdItemDirectory()) && getCategory() == item.getCategory() && Objects.equals(getCode(), item.getCode()) && Objects.equals(getTitle(), item.getTitle()) && getUnit() == item.getUnit() && Objects.equals(getPrice(), item.getPrice()) && Objects.equals(getVendor(), item.getVendor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getName(), getQuantity(), getCost(), getIdItemDirectory(), getCategory(), getCode(), getTitle(), getUnit(), getPrice(), getVendor());
    }

    public Item clone() throws CloneNotSupportedException{ return (Item) super.clone(); }
}
