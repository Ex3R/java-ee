package ru.nsu.g16207.terekhov.lab3.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nsu.g16207.terekhov.lab3.domain.TagEntity;

@Repository
public interface TagEntityRepository extends JpaRepository<TagEntity, Long> {

}
