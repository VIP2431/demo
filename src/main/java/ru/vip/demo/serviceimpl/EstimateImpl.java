package ru.vip.demo.serviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vip.demo.entity.*;
import ru.vip.demo.repository.*;
import ru.vip.demo.service.EstimateService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EstimateImpl implements EstimateService {

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
    public List<ItemDirectory> getAll() {
        return itemDirectoryRepository.findAll();
    }

    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public House save(House house) {
        return null;
    }

    @Override
    public Room save(Room room) {
        return null;
    }

    @Override
    public Section save(Section section) {
        return null;
    }

}
