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
    private TypeItem type = TypeItem.TYPE_ITEM; // Тип Блок/Позиция
    private Status status = Status.STAT_PUBLIC; // Статус
    private Unit unit = Unit.UN_NOT;            // Еденица измерения
    private String name;                        // Имя
    private String title;                       // Title

    private int flags;
    private int i1;
    private int i2;
    private int i3;
    private double d1;
    private double d2;
    private double d3;
    private double d4;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeadingItem)) return false;
        HeadingItem that = (HeadingItem) o;
        return getFlags() == that.getFlags()
                && getI1() == that.getI1()
                && getI2() == that.getI2()
                && getI3() == that.getI3()
                && Double.compare(that.getD1(), getD1()) == 0
                && Double.compare(that.getD2(), getD2()) == 0
                && Double.compare(that.getD3(), getD3()) == 0
                && Double.compare(that.getD4(), getD4()) == 0
                && getType() == that.getType()
                && getStatus() == that.getStatus()
                && getUnit() == that.getUnit()
                && getName().equals(that.getName())
                && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getStatus(), getUnit(), getName(), getTitle(),
                getFlags(), getI1(), getI2(), getI3(), getD1(), getD2(), getD3(), getD4());
    }
}
