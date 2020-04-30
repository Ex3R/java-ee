package ru.nsu.g16207.terekhov.lab3.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;
import ru.nsu.g16207.terekhov.lab3.repository.NodeEntityRepository;
import ru.nsu.g16207.terekhov.lab3.repository.TagEntityRepository;
import ru.nsu.g16207.terekhov.lab3.service.OSMService;

import java.util.Optional;

@Service
public class OsmServiceImpl implements OSMService {
    private static final Logger logger = LogManager.getLogger(OSMService.class.getName());
    private final NodeEntityRepository nodeEntityRepository;
    private final TagEntityRepository tagEntityRepository;

    public OsmServiceImpl(NodeEntityRepository nodeEntityRepository, TagEntityRepository tagEntityRepository) {
        this.nodeEntityRepository = nodeEntityRepository;
        this.tagEntityRepository = tagEntityRepository;
    }


    @Override
    public NodeEntity createNode(Node node) {
        return nodeEntityRepository.save(NodeEntity.of(node));
    }

    @Override
    public Optional<NodeEntity> findNodeByNodeId(Long nodeId) {
        return nodeEntityRepository.findByNodeId(nodeId);
    }

    @Override
    public Optional<NodeEntity> findNodeById(Integer id) {
        return nodeEntityRepository.findById(id);
    }

    @Override
    public NodeEntity createNode(NodeEntity nodeEntity) {
        return nodeEntityRepository.save(nodeEntity);
    }

    @Override
    public NodeEntity updateNode(NodeEntity nodeEntity) {
        return nodeEntityRepository.save(nodeEntity);
    }

    @Override
    public void removeNodeByNodeId(Long nodeId) {
        nodeEntityRepository.deleteById(nodeId);
    }
}
