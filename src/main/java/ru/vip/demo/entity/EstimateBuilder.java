package ru.vip.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@Builder
public class EstimateBuilder {
    private String nameNode;
    private String newName;
    private String titleNode;
    private List<String> nodes;
    private List<String> items;
}
