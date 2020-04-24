package ru.nsu.g16207.terekhov.lab2.dao;

import ru.nsu.g16207.terekhov.lab2.config.ConnectionFactory;
import ru.nsu.g16207.terekhov.lab2.model.osm.Node;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class NodeDaoImpl implements NodeDao {
    @Override
    public boolean insertNodeUsingPreparedStatement(Node node) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO nodes VALUES (default,?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, node.getId());
            ps.setInt(2, node.getVersion());
            Timestamp timestamp = new Timestamp(node.getTimestamp().toGregorianCalendar().getTimeInMillis());
            ps.setTimestamp(3, timestamp);
            ps.setInt(4, node.getUid());
            ps.setString(5, node.getUser());
            ps.setInt(6, node.getChangeset());
            ps.setDouble(7, node.getLat());
            ps.setDouble(8, node.getLon());

            int i = ps.executeUpdate();
            connection.close();

            return i == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean insertNodeUsingStatement(Node node) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement statement = connection.createStatement();

            Timestamp timestamp = new Timestamp(node.getTimestamp().toGregorianCalendar().getTimeInMillis());
            String query = String.format("INSERT INTO nodes VALUES (default, %d, %d, '%tF', %d, '%s', %d, %f, %f)",
                    node.getId(),
                    node.getVersion(),
                    timestamp,
                    node.getUid(),
                    node.getUser(),
                    node.getChangeset(),
                    node.getLat(),
                    node.getLon()
            );
            int i = statement.executeUpdate(query);
            return i == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void insertBatch(List<Node> nodes) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO nodes VALUES (default,?, ?, ?, ?, ?, ?, ?, ?)");
            int count = 0;

            for (Node node : nodes) {
                ps.setInt(1, node.getId());
                ps.setInt(2, node.getVersion());
                Timestamp timestamp = new Timestamp(node.getTimestamp().toGregorianCalendar().getTimeInMillis());
                ps.setTimestamp(3, timestamp);
                ps.setInt(4, node.getUid());
                ps.setString(5, node.getUser());
                ps.setInt(6, node.getChangeset());
                ps.setDouble(7, node.getLat());
                ps.setDouble(8, node.getLon());
                ps.addBatch();
                count++;

                if (count % 100 == 0 || count == nodes.size()) {
                    ps.executeBatch();
                    count = 0;
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public Optional<Node> getNodeById(Integer id) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM nodes WHERE id=" + id);
            if (rs.next()) {
                return Optional.ofNullable(extractNodeFromResultSet(rs));
            } else {
                return Optional.empty();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean updateNode(Node node) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE nodes SET id = ?, version = ?, timestamp = ?, uid = ?, user = ?, changeset = ?, lat = ?, lon = ? WHERE id = ?");

            ps.setInt(1, node.getId());
            ps.setInt(2, node.getVersion());
            Timestamp timestamp = new Timestamp(node.getTimestamp().toGregorianCalendar().getTimeInMillis());
            ps.setTimestamp(3, timestamp);
            ps.setInt(4, node.getUid());
            ps.setString(5, node.getUser());
            ps.setInt(6, node.getChangeset());
            ps.setDouble(7, node.getLat());
            ps.setDouble(8, node.getLon());

            int i = ps.executeUpdate();
            return i == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteNode(Integer id) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM nodes WHERE id=" + id);
            return i == 1;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
    /*    NodeDaoImpl nodeDao = new NodeDaoImpl();
        Node node = new Node();
        node.setId(321);
        node.setVersion(321);
        XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
        xmlGregorianCalendar.setTime(0, 0, 0);
        node.setTimestamp(xmlGregorianCalendar);
        node.setUser("roma");
        node.setUid(666);
        node.setChangeset(555);
        node.setLat(12.22);
        node.setLon(15.22);
        nodeDao.insertNodeUsingStatement(node);*/
    }
}
