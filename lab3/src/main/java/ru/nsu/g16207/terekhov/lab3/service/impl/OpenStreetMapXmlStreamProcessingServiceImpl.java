package ru.nsu.g16207.terekhov.lab3.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.nsu.g16207.terekhov.lab3.component.StaxStreamProcessor;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;
import ru.nsu.g16207.terekhov.lab3.service.OSMService;
import ru.nsu.g16207.terekhov.lab3.service.OpenStreetMapStreamXmlProcessingService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class OpenStreetMapXmlStreamProcessingServiceImpl implements OpenStreetMapStreamXmlProcessingService {
    private static final Logger logger = LogManager.getLogger(OpenStreetMapStreamXmlProcessingService.class.getName());
    private long amountOfNodes = 0;
    private final Unmarshaller unmarshaller;
    private final OSMService osmService;


    public OpenStreetMapXmlStreamProcessingServiceImpl(OSMService osmService) throws JAXBException {
        this.osmService = osmService;
        JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
        this.unmarshaller = jaxbContext.createUnmarshaller();
    }

    @Override
    public void streamXmlProcessing() {
        URL resource = getClass().getClassLoader().getResource("RU-NVS.osm");
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Paths.get(Objects.requireNonNull(resource).getPath())))) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                    amountOfNodes++;
                    Node node = (Node) unmarshaller.unmarshal(reader);
                    osmService.createNode(node);
                }
            }

            logger.info("reading xml was ended");


        } catch (XMLStreamException | IOException | JAXBException e) {
            logger.error("error on reading xml, {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


}


