package ru.nsu.g16207.terekhov.lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;

import java.util.Optional;

@Repository
public interface NodeEntityRepository extends JpaRepository<NodeEntity, Long> {

    Optional<NodeEntity> findByNodeId(Long nodeId);

    Optional<NodeEntity> findById(Integer id);
}
