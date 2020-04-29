package ru.nsu.g16207.terekhov.lab3.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.g16207.terekhov.lab3.service.OpenStreetMapStreamXmlProcessingService;

@RestController
@Validated
@RequestMapping("test")
public class TestController {

    private final OpenStreetMapStreamXmlProcessingService openStreetMapStreamXmlProcessingService;

    public TestController(OpenStreetMapStreamXmlProcessingService openStreetMapStreamXmlProcessingService) {
        this.openStreetMapStreamXmlProcessingService = openStreetMapStreamXmlProcessingService;
    }

    @GetMapping
    public void saveNode() {
        openStreetMapStreamXmlProcessingService.streamXmlProcessing();
    }
}
