package ru.nsu.g16207.terekhov.lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;

@Repository
public interface NodeEntityRepository extends JpaRepository<NodeEntity, Long> {
}
