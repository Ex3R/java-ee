package ru.nsu.g16207.terekhov.lab3.service;

import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;

import java.util.Optional;

public interface OSMService {

    NodeEntity createNode(Node node);

    Optional<NodeEntity> findNodeByNodeId(Long nodeId);

    Optional<NodeEntity> findNodeById(Integer id);

    NodeEntity createNode(NodeEntity nodeEntity);

    NodeEntity updateNode(NodeEntity nodeEntity);

    void removeNodeByNodeId(Long nodeId);


}
