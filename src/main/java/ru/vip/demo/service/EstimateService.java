package ru.vip.demo.service;

import org.springframework.stereotype.Service;
import ru.vip.demo.entity.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface EstimateService {

    public Item save(Item item);
    public ItemDirectory save(ItemDirectory itemDirectory);
    public Node save(Node node);

    public List<ItemDirectory> getAllItemDirectory();
    public List<Item> getAllItem();
    public List<Node> getAllNode();
    public Optional<Node> findByIdNode(UUID id);

    public List<ItemDirectory> readJsonItemDirectory( String resourceName) throws IOException;

    public void writeJsonItemDirectory( String outFile, List<ItemDirectory> itemDirectories) throws IOException;

    public List<Item> readJsonItem( String resourceName) throws IOException;

    public void writeJsonItem( String outFile, List<Item> itemList) throws IOException;

    public List<Node> readJsonNode( String resourceName) throws IOException;

    public void writeJsonNode( String outFile, List<Node> nodeList) throws IOException;

    public List<MainBuilder> readJsonBuilder(String resourceName) throws IOException;

    public void writeJsonBuilder( String outFile, List<MainBuilder> builderList) throws IOException;
}
