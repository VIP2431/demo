package ru.vip.demo.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vip.demo.entity.Node;
import ru.vip.demo.type.TypeItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NodeRepository extends JpaRepository<Node, UUID> {
    Optional<List<Node>>  findByType(TypeItem typeItem, Sort name);
}
