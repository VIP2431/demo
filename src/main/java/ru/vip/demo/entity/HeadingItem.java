package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.type.Status;
import ru.vip.demo.type.TypeItem;
import ru.vip.demo.type.Unit;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class HeadingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    //    @NonNull
    private TypeItem type = TypeItem.TYPE_ITEM;         // Тип Блока/позиции
    private Status status = Status.STAT_PUBLIC;         // Статус

    private String title;                               // Title
    private String name;                                // Имя

    private int percentItem;                            // Процент исполнения - процентовка
    private Unit unit = Unit.UN_METER_2;                // Еденица измерения
    private BigDecimal quantity;                        // Количество
    private BigDecimal price; //= BigDecimal.valueOf(0);// Цена еденицы
    private BigDecimal cost;                            // Стоимость
    private BigDecimal sum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeadingItem)) return false;
        HeadingItem that = (HeadingItem) o;
        return getPercentItem() == that.getPercentItem()
                && getType() == that.getType()
                && getStatus() == that.getStatus()
                && getTitle().equals(that.getTitle())
                && getName().equals(that.getName())
                && getUnit() == that.getUnit()
                && Objects.equals(getQuantity(), that.getQuantity())
                && Objects.equals(getPrice(), that.getPrice())
                && Objects.equals(getCost(), that.getCost())
                && Objects.equals(getSum(), that.getSum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getStatus(), getTitle(), getName()
                , getPercentItem(), getUnit(), getQuantity()
                , getPrice(), getCost(), getSum());
    }
}
