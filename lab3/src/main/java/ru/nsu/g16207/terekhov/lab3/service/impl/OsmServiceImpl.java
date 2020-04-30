package ru.nsu.g16207.terekhov.lab3.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;
import ru.nsu.g16207.terekhov.lab3.repository.NodeEntityRepository;
import ru.nsu.g16207.terekhov.lab3.repository.TagEntityRepository;
import ru.nsu.g16207.terekhov.lab3.service.OSMService;

import java.util.List;
import java.util.Optional;

@Service
public class OsmServiceImpl implements OSMService {
    private static final Logger logger = LogManager.getLogger(OSMService.class.getName());
    private static final int pageSize = 100;
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

    @Override
    public List<NodeEntity> getAll(int page) {
        Page<NodeEntity> nodeEntityPage = nodeEntityRepository.findAllBy(PageRequest.of(page, pageSize));
        int totalPages = nodeEntityPage.getTotalPages();
        if (page >= totalPages) {
            return null;
        }

        return nodeEntityPage.getContent();
    }

    @Override
    public List<NodeEntity> getNodesByLocationInRadius(float lat, float lon, int radius, int page) {
        Page<NodeEntity> nodeEntityPage = nodeEntityRepository.findAllNodesByLocationAndRadius(lat, lon, radius, PageRequest.of(page, pageSize));
        int totalPages = nodeEntityPage.getTotalPages();
        if (page >= totalPages) {
            return null;
        }

        return nodeEntityPage.getContent();
    }
}
