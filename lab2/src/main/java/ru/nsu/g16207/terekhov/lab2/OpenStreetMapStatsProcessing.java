package ru.nsu.g16207.terekhov.lab2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.g16207.terekhov.lab2.model.UidMarks;
import ru.nsu.g16207.terekhov.lab2.model.UserChanges;
import ru.nsu.g16207.terekhov.lab2.model.osm.Node;

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
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class OpenStreetMapStatsProcessing {
    private static final Logger logger = LogManager.getLogger(OpenStreetMapStatsProcessing.class.getName());
    private Map<String, Long> userChangesMap;
    private Map<Integer, Long> uidMarksMap;
    private long amountOfNodes = 0;
    private JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
    private Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

    public OpenStreetMapStatsProcessing() throws JAXBException {
        userChangesMap = new HashMap<>();
        uidMarksMap = new HashMap<>();
    }

    public void processStats() {
        URL resource = getClass().getClassLoader().getResource("RU-NVS.osm");
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Paths.get(Objects.requireNonNull(resource).getPath())))) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                    amountOfNodes++;
                    Node node = (Node) unmarshaller.unmarshal(reader);
                    userChangesMap.compute(node.getUser(), (k, v) -> (v == null) ? 1 : 1 + v);
                    uidMarksMap.compute(node.getUid(), (k, v) -> (v == null) ? 1 : 1 + v);
                }
            }

            logger.info("processing stats was ended");


            List<UserChanges> sortedUserChangesList = userChangesMap.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .map((entry) -> new UserChanges(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            List<UidMarks> sortedUidMarksList = uidMarksMap.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .map((entry) -> new UidMarks(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());


            logger.info(format("list with user and his amount of changes size is %s", sortedUserChangesList.size()));
            logger.info(format("amount of nodes wit user changes info is %s", amountOfNodes));


            StringBuilder userChangesOutput = new StringBuilder("first\n");
            sortedUserChangesList.stream().limit(50).forEach(userChanges -> {
                logger.info((format("User %s amount of changes is %s", userChanges.getUser(), userChanges.getAmountOfChanges())));
                userChangesOutput
                        .append(format("User %s amount of changes is %s", userChanges.getUser(), userChanges.getAmountOfChanges()))
                        .append("\n");

            });

            StringBuilder uidMarksOutput = new StringBuilder("second\n");
            sortedUidMarksList.stream().limit(50).forEach(uidMarks -> {
                logger.info((format("Uid %s amount of marks is %s", uidMarks.getUid(), uidMarks.getAmountOfMarks())));
                uidMarksOutput
                        .append((format("Uid %s amount of marks is %s", uidMarks.getUid(), uidMarks.getAmountOfMarks())))
                        .append("\n");
            });

            System.out.println(userChangesOutput);
            System.out.println(uidMarksOutput);

        } catch (XMLStreamException | IOException | JAXBException e) {
            logger.error("error on processing xml, {}", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }


}


