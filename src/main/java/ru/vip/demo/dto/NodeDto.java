package ru.vip.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.vip.demo.entity.Item;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NodeDto implements Serializable {
    String name = null;        // Название

    private List<NodeDto> listNode;

    private List <Item> listItem;
}
