/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.nsu.g16207.terekhov.lab2;


import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBException;

@Data
public class App {
    private static final Logger logger = LogManager.getLogger(App.class.getName());
    private OpenStreetMapStatsProcessing openStreetMapStatsProcessing;

    public String getGreeting() {
        return "Processing stats was started.";
    }

    public App() throws JAXBException {
        openStreetMapStatsProcessing = new OpenStreetMapStatsProcessing();
    }


    public static void main(String[] args) throws JAXBException {
        App app = new App();
        app.getGreeting();
        app.openStreetMapStatsProcessing.processStats();
    }
}
