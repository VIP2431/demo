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

    private final HouseRepository houseRepository;
    private final RoomRepository roomRepository;
    private final SectionRepository sectionRepository;

    @Override
    public ItemDirectory save(ItemDirectory itemDirectory) {
        return itemDirectoryRepository.save(itemDirectory);
    }
    @Override
    public Item save(Item item) { return itemRepository.save(item); }
    @Override
    public House save(House house) { return houseRepository.save(house); }
    @Override
    public Room save(Room room) { return roomRepository.save(room); }
    @Override
    public Section save(Section section) { return sectionRepository.save(section); }

    @Override
    public List<ItemDirectory> getAllItemDirectory() { return itemDirectoryRepository.findAll(); }
    @Override
    public List<Item> getAllItem() { return itemRepository.findAll(); }
    @Override
    public List<House> getAllHouse() { return houseRepository.findAll(); }
    @Override
    public List<Room> getAllRoom() { return roomRepository.findAll(); }
    @Override
    public List<Section> getAllSection() { return sectionRepository.findAll(); }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Десериализация в List объектов из JSON файла
    private InputStream inputStream( String resourceName) throws IOException { // Открыть поток чтения
        return Resources.getResource(resourceName).openStream();
    }

    public List<ItemDirectory> readJSON( String resourceName) throws IOException {
        return mapper.readValue(inputStream(resourceName), new TypeReference<List<ItemDirectory>>() {});
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////
//        JSON Сериализация из List объектов в JSON файл
    public void writeJSON( String outFile, List<ItemDirectory> itemDirectories) throws IOException {
        mapper.writeValue( new FileOutputStream(outFile), itemDirectories);
    }

}
