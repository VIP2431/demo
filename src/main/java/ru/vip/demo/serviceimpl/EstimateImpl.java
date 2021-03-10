package ru.vip.demo.serviceimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.io.Resources;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.*;
import ru.vip.demo.repository.*;
import ru.vip.demo.service.EstimateService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EstimateImpl implements EstimateService {

    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); // pretty print JSON

    private final ItemDirectoryRepository itemDirectoryRepository;
    private final ItemRepository itemRepository;
    private final NodeRepository nodeRepository;

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

    public List<ItemDirectory> readJSON( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<ItemDirectory>>() {});
    }

    public List<Node> readJsonNode( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<Node>>() {});
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Сериализация из List объектов в JSON файл
    public void writeJSON( String outFile, List<ItemDirectory> itemDirectories) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), itemDirectories);
    }

   public void writeJsonNode( String outFile, List<Node> nodeList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), nodeList);
    }

}
