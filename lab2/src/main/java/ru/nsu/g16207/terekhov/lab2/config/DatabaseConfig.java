package ru.nsu.g16207.terekhov.lab2.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final Logger logger = LogManager.getLogger(DatabaseConfig.class.getName());
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/";
    static final String USER = "postgres";
    static final String PASS = "postgres";

    public static void initDatabeses() {
        logger.info("started script init database");
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            PreparedStatement pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS nodes (\n" +
                    "                                       id INTEGER PRIMARY KEY,\n" +
                    "                                       version INTEGER,\n" +
                    "                                       timestamp date,\n" +
                    "                                       uid INTEGER,\n" +
                    "                                       \"user\" VARCHAR(100),\n" +
                    "                                       changeset INTEGER,\n" +
                    "                                       lat double precision,\n" +
                    "                                       lon double precision\n" +
                    "\n" +
                    "\n" +
                    ")");
            pst.execute();

            pst = con.prepareStatement("CREATE TABLE IF NOT EXISTS tags (\n" +
                    "                                     id serial PRIMARY KEY,\n" +
                    "                                     key VARCHAR(100),\n" +
                    "                                     value VARCHAR(100),\n" +
                    "                                     constraint node_id foreign key (id) references nodes (id)\n" +
                    ");");
            pst.execute();

            logger.info("databased created successfully");

        } catch (SQLException ex) {
            logger.error("error on creating databases");
            ex.printStackTrace();
        }
    }

    public static void dropDatabases() {
        logger.info("started script drop database");
        try (Connection con = DriverManager.getConnection(DB_URL, USER, PASS)) {
            PreparedStatement pst = con.prepareStatement("DROP DATABASE nodes");
            pst.execute();
            pst = con.prepareStatement("DROP DATABASE tags");
            pst.execute();

            logger.info("databased deletes successfully");

        } catch (SQLException ex) {
            logger.error("error on deleting databases");
            ex.printStackTrace();
        }
    }


}
