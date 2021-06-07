package ru.vip.demo.service;

import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
public interface EstimateService {

    Item save(Item item);
    void save(ItemDirectory itemDirectory);
    Node save(Node node);

    List<ItemDirectory> getAllItemDirectory();
    List<Item> getAllItem();
    Optional<Item> findByIdItem(UUID id);

    List<Node> getAllNode();
    Optional<Node> findByIdNode(UUID id);

    List<ItemDirectory> readJsonItemDirectory( String resourceName) throws IOException;

    void writeJsonItemDirectory( String outFile, List<ItemDirectory> itemDirectories) throws IOException;

    List<Item> readJsonItem( String resourceName) throws IOException;

    public void writeJsonItem( String outFile, List<Item> itemList) throws IOException;

    public List<Node> readJsonNode( String resourceName) throws IOException;

    public void writeJsonNode( String outFile, List<Node> nodeList) throws IOException;

    public List<MainBuilder> readJsonBuilder(String resourceName) throws IOException;

    public void writeJsonBuilder( String outFile, List<MainBuilder> builderList) throws IOException;
}
