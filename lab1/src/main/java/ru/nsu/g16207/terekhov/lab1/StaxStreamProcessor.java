package ru.nsu.g16207.terekhov.lab1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

import static java.lang.String.format;

public class StaxStreamProcessor implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(StaxStreamProcessor.class.getName());
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        reader = FACTORY.createXMLStreamReader(is);
    }

    public XMLStreamReader getReader() {
        return reader;
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                logger.error(format("error on stax stream processing, cause %s", e.getLocalizedMessage()));
            }
        }
    }
}