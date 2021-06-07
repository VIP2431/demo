package ru.vip.demo.type;

//@Data
public enum Unit { // Еденицы измерения
    UN_NOT("Нет единицы измерения"),
    UN_METER_LINE("Погонный метр"),
    UN_METER_2("Квадратный метр"),
    UN_METER_3("Кубический метр"),
    UN_KG("Килограмм"),
    UN_LITRE("Литров"),
    UN_HOUR("Час"),
    UN_STEP("Шаг"),
    UN_THING("Штук"),
    UN_TIME("Раз");

    private String name;

    Unit(String name) {
        this.name = name;
    }
}
