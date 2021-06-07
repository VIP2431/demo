package ru.vip.demo.type;

public enum TypeItem {
    TYPE_HOUSE("Объект"),
    TYPE_ROOM("Комната"),
    TYPE_BLOCK("Блок работ"),
    TYPE_SUM("Блок сумм"),
    TYPE_COM("Общий блок"),
    TYPE_ITEM("Позиция сметы");

    private String name;

    TypeItem(String name) { this.name = name; }

    public String getName() { return name; }
}
