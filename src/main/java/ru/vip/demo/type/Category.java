package ru.vip.demo.type;

public enum Category {
    CTG_NOT(" "),
    CTG_WORK("Работа"),
    CTG_SERVICE("Сервис"),
    CTG_MATERIAL("Материалы"),
    CTG_TOOLS("Инструменты"),
    CTG_SUM("Сумма"),
    CTG_COM("Общие итоги");

    private String name;

    Category(String name) {
        this.name = name;
    }

}
