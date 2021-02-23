package ru.vip.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vip.demo.entity.Item;

import java.util.UUID;

@Repository
public interface ItemRepository  extends JpaRepository<Item, UUID> {
}
