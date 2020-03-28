package ru.nsu.g16207.terekhov.lab1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nsu.g16207.terekhov.lab1.model.UidMarks;
import ru.nsu.g16207.terekhov.lab1.model.UserChanges;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class OpenStreetMapStatsProcessing {
    private static final Logger logger = LogManager.getLogger(OpenStreetMapStatsProcessing.class.getName());
    private Map<String, Long> userChangesMap;
    private Map<String, Long> uidMarksMap;
    private List<UserChanges> sortedUserChangesList;
    private List<UidMarks> sortedUidMarksList;
    private long amountOfNodes = 0;

    public OpenStreetMapStatsProcessing() {
        userChangesMap = new HashMap<>();
        uidMarksMap = new HashMap<>();
    }

    public void processStats() {
        URL resource = getClass().getClassLoader().getResource("RU-NVS.osm");
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Files.newInputStream(Path.of(Objects.requireNonNull(resource).getPath())))) {
            XMLStreamReader reader = processor.getReader();
            while (reader.hasNext()) {       // while not end of XML
                int event = reader.next();
                if (event == XMLEvent.START_ELEMENT && "node".equals(reader.getLocalName())) {
                    amountOfNodes++;
                    int userNameIndex = -1;
                    int uidIndex = -1;
                    for (int i = 0; i < reader.getAttributeCount(); i++) {
                        final String attributeName = reader.getAttributeLocalName(i);
                        if ("user".equals(attributeName)) {
                            userNameIndex = i;
                        }

                        if ("uid".equals(attributeName)) {
                            uidIndex = i;
                        }
                    }

                    if (userNameIndex == -1) {
                        logger.error("in node property 'user' not found");
                    } else {
                        String user = reader.getAttributeValue(userNameIndex);
                        if (userChangesMap.containsKey(user)) {
                            long userChanges = userChangesMap.get(user);
                            logger.debug(format("User with name %s already in map, his changes amount is %s", user, userChanges));
                            userChangesMap.put(user, ++userChanges);
                        } else {
                            logger.debug(format("User with name %s found first time, his changes amount is initial", user));
                            userChangesMap.put(user, 0L);
                        }
                    }

                    if (uidIndex == -1) {
                        logger.error("in node property 'uid' not found");
                    } else {
                        String uid = reader.getAttributeValue(uidIndex);
                        if (uidMarksMap.containsKey(uid)) {
                            long marks = uidMarksMap.get(uid);
                            logger.debug(format("Uid %s already in map, his nodes marks amount is %s", uid, marks));
                            uidMarksMap.put(uid, ++marks);
                        } else {
                            logger.debug(format("Uid %s found first time, his nodes marks is initial", uid));
                            userChangesMap.put(uid, 0L);
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
            logger.info((format("Uid %s amount of marks is %s", uidMarks.getUid(), uidMarks.getUid())));
            uidMarksOutput
                    .append((format("Uid %s amount of marks is %s", uidMarks.getUid(), uidMarks.getUid())))
                    .append("\n");
        });

        System.out.println(userChangesOutput);
        System.out.println(uidMarksOutput);
    }
}
