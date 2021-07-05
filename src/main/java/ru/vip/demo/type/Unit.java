package ru.vip.demo.type;

//@Data
public enum Unit { // Еденицы измерения
    UN_NOT(" "),
    UN_METER_LINE("М"),
    UN_METER_2("М2"),
    UN_METER_3("М3"),
    UN_KG("Кг"),
    UN_LITRE("Литр"),
    UN_HOUR("Час"),
    UN_STEP("Шаг"),
    UN_THING("Штук"),
    UN_RUBLE_RF("Руб"),
    UN_TIME("Раз");

    private String name;

    Unit(String name) {
        this.name = name;
    }
}
