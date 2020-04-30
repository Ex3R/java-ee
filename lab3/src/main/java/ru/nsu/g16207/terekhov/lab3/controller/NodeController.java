package ru.nsu.g16207.terekhov.lab3.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;
import ru.nsu.g16207.terekhov.lab3.service.OSMService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("node")
public class NodeController {
    private static final Logger logger = LogManager.getLogger(NodeController.class.getName());
    private final OSMService osmService;


    public NodeController(OSMService osmService) {
        this.osmService = osmService;
    }

    @PostMapping("create")
    public ResponseEntity<NodeEntity> createNewNode(@Valid @RequestBody Node node) {
        return new ResponseEntity<>(osmService.createNode(node), HttpStatus.CREATED);
    }

    @GetMapping("getAll")
    public ResponseEntity<List<NodeEntity>> getAllNodeEntities(@RequestParam int page) {
        return new ResponseEntity<>(osmService.getAll(page), HttpStatus.OK);
    }

    @GetMapping("getByNodeId")
    public ResponseEntity<NodeEntity> getByNodeId(@RequestParam long nodeId) {
        return new ResponseEntity<>(osmService.findNodeByNodeId(nodeId).orElse(null), HttpStatus.OK);
    }

    @GetMapping("getById")
    public ResponseEntity<NodeEntity> getByNodeId(@RequestParam int id) {
        return new ResponseEntity<>(osmService.findNodeById(id).orElse(null), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<NodeEntity> updateNode(@Valid @RequestBody NodeEntity nodeEntity) {
        return new ResponseEntity<>(osmService.updateNode(nodeEntity), HttpStatus.OK);
    }

    @PostMapping("delete")
    public ResponseEntity deleteNode(@RequestParam long nodeId) {
        osmService.removeNodeByNodeId(nodeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("getNodesByLocationInRadius")
    public ResponseEntity<List<NodeEntity>> getNodesByLocationInRadius(
            @RequestParam(value = "lat", required = false) Float lat,
            @RequestParam(value = "lon", required = false) Float lon,
            @RequestParam(value = "radius", required = false) Integer radius,
            @RequestParam(value = "page", required = false) Integer page) {
        //for test
        List<NodeEntity> nodes = osmService.getNodesByLocationInRadius(55.0282215f, 82.9234476f, 100000, 0);
        //List<NodeEntity> nodes = osmService.getNodesByLocationInRadius(lat, lon, radius, page);
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }


}
