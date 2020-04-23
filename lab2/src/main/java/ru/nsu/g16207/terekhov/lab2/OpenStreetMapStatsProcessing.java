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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

public class OpenStreetMapStatsProcessing {
    private static final Logger logger = LogManager.getLogger(OpenStreetMapStatsProcessing.class.getName());
    private Map<String, Long> userChangesMap;
    private Map<Integer, Long> uidMarksMap;
    private List<UserChanges> sortedUserChangesList;
    private List<UidMarks> sortedUidMarksList;
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
            while (reader.hasNext()) {       // while not end of XML
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                    Node node = (Node) unmarshaller.unmarshal(reader);

                    userChangesMap.compute(node.getUser(), (k, v) ->)
                    if (userChangesMap.containsKey(node.getUser())) {
                        long userChanges = userChangesMap.get(node.getUser());
                        logger.debug(format("User with name %s already in map, his changes amount is %s", node.getUser(), userChanges));
                        userChangesMap.put(node.getUser(), ++userChanges);
                    } else {
                        logger.debug(format("User with name %s found first time, his changes amount is initial", node.getUser()));
                        userChangesMap.put(node.getUser(), 0L);
                    }

                    if (uidMarksMap.containsKey(node.getUid())) {
                        long marks = uidMarksMap.get(node.getUid());
                        logger.debug(format("Uid %s already in map, his nodes marks amount is %s", node.getUid(), marks));
                        uidMarksMap.put(node.getUid(), ++marks);
                    } else {
                        logger.debug(format("Uid %s found first time, his nodes marks is initial", node.getUid()));
                        uidMarksMap.put(node.getUid(), 0L);
                    }

                }
            }

        } catch (XMLStreamException | IOException | JAXBException e) {
            e.printStackTrace();
        }
    }

}

           /* Node node = null;
            while (streamReader.hasNext()) {       // while not end of XML
                XMLEvent event = streamReader.nextEvent();
                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals("node")) {
                        node = new Node();
                        Iterator<Attribute> attributes = startElement
                                .getAttributes();

                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals("id")) {
                                node.setId(attribute.getValue());
                                continue;
                            }

                            if (attribute.getName().toString().equals("version")) {
                                node.setVersion(attribute.getValue());
                                continue;
                            }

                            if (attribute.getName().toString().equals("timestamp")) {
                                node.setTimestamp(attribute.getValue());
                                continue;
                            }

                            if (attribute.getName().toString().equals("uid")) {
                                node.setUid(attribute.getValue());
                                continue;
                            }


                            if (attribute.getName().toString().equals("user")) {
                                node.setUser(attribute.getValue());
                                continue;
                            }


                            if (attribute.getName().toString().equals("changeset")) {
                                node.setChangeset(attribute.getValue());
                                continue;
                            }

                            if (attribute.getName().toString().equals("lat")) {
                                node.setLat(Double.valueOf(attribute.getValue()));
                                continue;
                            }

                            if (attribute.getName().toString().equals("lon")) {
                                node.setLon(Double.valueOf(attribute.getValue()));
                                continue;
                            }
                        }

                        if (userChangesMap.containsKey(node.getUser())) {
                            long userChanges = userChangesMap.get(node.getUser());
                            logger.debug(format("User with name %s already in map, his changes amount is %s", node.getUser(), userChanges));
                            userChangesMap.put(node.getUser(), ++userChanges);
                        } else {
                            logger.debug(format("User with name %s found first time, his changes amount is initial", node.getUser()));
                            userChangesMap.put(node.getUser(), 0L);
                        }

                        if (uidMarksMap.containsKey(node.getUid())) {
                            long marks = uidMarksMap.get(node.getUid());
                            logger.debug(format("Uid %s already in map, his nodes marks amount is %s", node.getUid(), marks));
                            uidMarksMap.put(node.getUid(), ++marks);
                        } else {
                            logger.debug(format("Uid %s found first time, his nodes marks is initial", node.getUid()));
                            uidMarksMap.put(node.getUid(), 0L);
                        }

                    }
                }
            }
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
            logger.error("error on processing stats");

        }

        logger.info("processing stats was ended");


        sortedUserChangesList = userChangesMap.entrySet().parallelStream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map((entry) -> new UserChanges(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        sortedUidMarksList = uidMarksMap.entrySet().parallelStream()
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
    }*/

