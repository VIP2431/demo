package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
public class MainBuilder {
    private String nameNode;
    private String newName;
    private String titleNode;
    private List<String> nodes;
    private List<String> items;
}
