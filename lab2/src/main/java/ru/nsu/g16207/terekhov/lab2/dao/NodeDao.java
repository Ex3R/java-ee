package ru.nsu.g16207.terekhov.lab2.dao;

import ru.nsu.g16207.terekhov.lab2.model.osm.Node;

import java.util.Optional;

public interface NodeDao {

    boolean insertNodeUsingPreparedStatement(Node node);

    boolean insertNodeUsingStatement(Node node);

    Optional<Node> getNodeById(Integer id);

    boolean updateNode(Node node);

    boolean deleteNode(Integer id);
}
