package ru.vip.demo.type;

public enum Status {
    STAT_PUBLIC("PUBLIC"),
    STAT_NO_MACH("NO_MACH"),
    STAT_NO_VISIBLE("NO_VISIBLE");

    private String name;

    Status(String name) { this.name = name; }

    public String getName() { return this.name; }
}
