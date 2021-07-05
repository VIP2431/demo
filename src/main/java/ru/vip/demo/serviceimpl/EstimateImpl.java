package ru.vip.demo.serviceimpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.dto.ItemDto;
import ru.vip.demo.entity.Item;
import ru.vip.demo.entity.MainBuilder;
import ru.vip.demo.entity.Node;
import ru.vip.demo.repository.ItemRepository;
import ru.vip.demo.repository.NodeRepository;
import ru.vip.demo.service.EstimateService;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Repository
@Transactional
@RequiredArgsConstructor
//@Slf4j
public class EstimateImpl implements EstimateService{

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT); // pretty print JSON

    private final String ID_NULL_UUID = "00000000-0000-0000-0000-000000000000";
    private final UUID UUID_NULL = UUID.fromString(ID_NULL_UUID);

    private final ItemRepository itemRepository;
    private final NodeRepository nodeRepository;

//    private final NodeService nodeService;
//
//    public Node cloneNode(Node node) { return nodeService.cloneNode(node); }
//    public Node addNodeToNode(Node node, int poz) { return nodeService.addNodeToNode(node, poz); }
//    public Node addItemToNode(Node node, Item item) { return nodeService.addItemToNode(node,item); }

    public String getID_NULL_UUID() { return this.ID_NULL_UUID; }
    public UUID getUUID_NULL() { return  this.UUID_NULL; }

    @Override
    public Item save(Item item) { return itemRepository.save(item); }
    @Override
    public Node save(Node node) { return nodeRepository.save(node); }

    @Override
    public List<Item> getAllItem() { return itemRepository.findAll(); }
    @Override
    public Optional<Item> findByIdItem(UUID id) { return itemRepository.findById(id); }

    @Override
    public List<Node> getAllNode() { return nodeRepository.findAll(); }
    @Override
    public Optional<Node> findByIdNode(UUID id) { return nodeRepository.findById(id); }



/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Десериализация в List объектов из JSON файла
    private PushbackReader inputStream(String resourceName) throws IOException { // Открыть поток чтения
        return new PushbackReader( new FileReader( resourceName, StandardCharsets.UTF_8));
    }

    public List<Node> readJsonNode( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<>() {});
    }

    public List<Item> readJsonItem( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<>() { });
    }

    public List<MainBuilder> readJsonBuilder(String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<>() { });
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Сериализация из List объектов в JSON файл

   public void writeJsonNode( String outFile, List<Node> nodeList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), nodeList);
    }

   public void writeJsonItem( String outFile, List<Item> itemList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), itemList);
    }

   public void writeJsonItemDto( String outFile, List<ItemDto> itemDtoList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), itemDtoList);
    }

   public void writeJsonBuilder (String outFile, List<MainBuilder> builderList) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), builderList);
    }

}
