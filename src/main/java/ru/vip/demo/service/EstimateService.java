package ru.vip.demo.service;

import org.springframework.stereotype.Service;
import ru.vip.demo.entity.*;

import java.util.List;

@Service
public interface EstimateService {
//
//    public <T> save(T t);
//    public List<T> getAll();
//
    public House save(House house);
    public Item save(Item item);
    public ItemDirectory save(ItemDirectory itemDirectory);
    public Room save(Room room);
    public Section save(Section section);

    public List<ItemDirectory> getAllItemDirectory();
    public List<Item> getAllItem();
    public List<House> getAllHouse();
    public List<Room> getAllRoom();
    public List<Section> getAllSection();

}
