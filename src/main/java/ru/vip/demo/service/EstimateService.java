package ru.vip.demo.service;

import ru.vip.demo.dto.ItemDto;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstimateService{

//    Node cloneNode(Node node);
//    Node addNodeToNode(Node node, int poz);
//    Node addItemToNode(Node node, Item item);

    Item save(Item item);
    Node save(Node node);

    List<Item> getAllItem();
    Optional<Item> findByIdItem(UUID id);

    List<Node> getAllNode();
    Optional<Node> findByIdNode(UUID id);

    List<Item> readJsonItem( String resourceName) throws IOException;

    void writeJsonItem( String outFile, List<Item> itemList) throws IOException;

    void writeJsonItemDto( String outFile, List<ItemDto> itemDtoList) throws IOException;

    List<Node> readJsonNode( String resourceName) throws IOException;

    void writeJsonNode( String outFile, List<Node> nodeList) throws IOException;

    List<MainBuilder> readJsonBuilder(String resourceName) throws IOException;

    void writeJsonBuilder( String outFile, List<MainBuilder> builderList) throws IOException;
}
