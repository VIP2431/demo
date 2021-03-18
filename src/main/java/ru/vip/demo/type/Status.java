package ru.vip.demo.type;

// @Data
public enum Status {
    STAT_HOUSE("Объект"),
    STAT_ROOM("Комната"),
    STAT_BLOCK("Блок работ"),
    STAT_SUM("Блок сумм"),
    STAT_COM("Общий блок");

    private String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() { return this.name; }
}
