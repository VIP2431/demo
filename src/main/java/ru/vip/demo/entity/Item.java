package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Category;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item extends HeadingItem implements Serializable, Cloneable {
    private UUID idItemDirectory;
    private Category category;      // Категория товара
    private String code;            // Код товара
    private String vendor;          // Код поставщика товара

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        if (!super.equals(o)) return false;
        Item item = (Item) o;
        return getIdItemDirectory().equals(item.getIdItemDirectory())
                && getCategory() == item.getCategory()
                && getCode().equals(item.getCode())
                && getVendor().equals(item.getVendor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdItemDirectory()
                , getCategory(), getCode(), getVendor());
    }

    public Item clone() throws CloneNotSupportedException{ return (Item) super.clone(); }
}
