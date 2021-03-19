package ru.vip.demo.serviceimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.io.Resources;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.EstimateBuilder;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.ItemDirectory;
import ru.vip.demo.entity.Node;
import ru.vip.demo.repository.ItemDirectoryRepository;
import ru.vip.demo.repository.ItemRepository;
import ru.vip.demo.repository.NodeRepository;
import ru.vip.demo.service.EstimateService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EstimateImpl implements EstimateService {

    private ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); // pretty print JSON

    private final String idNullUUID = "00000000-0000-0000-0000-000000000000";
    private final UUID uuidNull = UUID.fromString(idNullUUID);

    private final ItemDirectoryRepository itemDirectoryRepository;
    private final ItemRepository itemRepository;
    private final NodeRepository nodeRepository;

    public ObjectMapper getMapper() { return this.mapper; }

    public String getIdNullUUID() { return this.idNullUUID; }
    public UUID getUuidNull() { return  this.uuidNull; };

    @Override
    public ItemDirectory save(ItemDirectory itemDirectory) {
        return itemDirectoryRepository.save(itemDirectory);
    }
    @Override
    public Item save(Item item) { return itemRepository.save(item); }
      @Override
    public Node save(Node node) { return nodeRepository.save(node); }

    @Override
    public List<ItemDirectory> getAllItemDirectory() { return itemDirectoryRepository.findAll(); }
    @Override
    public List<Item> getAllItem() { return itemRepository.findAll(); }
    @Override
    public List<Node> getAllNode() { return nodeRepository.findAll(); }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Десериализация в List объектов из JSON файла
    private InputStream inputStream( String resourceName) throws IOException { // Открыть поток чтения
        return Resources.getResource(resourceName).openStream();
    }

    public List<ItemDirectory> readJsonItemDirectory( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<ItemDirectory>>() {});
    }

    public List<Node> readJsonNode( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<Node>>() {});
    }

    public List<Item> readJsonItem( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<Item>>() {});
    }

    public List<EstimateBuilder> readJsonBuilder( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<EstimateBuilder>>() {});
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Сериализация из List объектов в JSON файл
    public void writeJsonItemDirectory( String outFile, List<ItemDirectory> itemDirectories) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), itemDirectories);
    }

   public void writeJsonNode( String outFile, List<Node> nodeList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), nodeList);
    }

   public void writeJsonItem( String outFile, List<Item> itemList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), itemList);
    }

   public void writeJsonBuilder (String outFile, List<EstimateBuilder> builderList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), builderList);
    }

}
