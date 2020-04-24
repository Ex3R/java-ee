package ru.nsu.g16207.terekhov.lab2.dao;

import ru.nsu.g16207.terekhov.lab2.model.osm.Node;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NodeDao {

    boolean insertNodeUsingPreparedStatement(Node node);

    boolean insertNodeUsingStatement(Node node);

    void insertBatch(List<Node> nodes);

    Optional<Node> getNodeById(Integer id);

    boolean updateNode(Node node);

    boolean deleteNode(Integer id);


    default Node extractNodeFromResultSet(ResultSet rs) throws Exception {
        Node node = new Node();
        node.setId(rs.getInt("id"));
        node.setVersion(rs.getInt("version"));
        XMLGregorianCalendar xmlGregorianCalendar = convertLocalDataTimeToXmlGCal(rs.getTimestamp("timestamp"));
        node.setTimestamp(xmlGregorianCalendar);
        node.setUid(rs.getInt("uid"));
        node.setUser(rs.getString("user"));
        node.setChangeset(rs.getInt("changeset"));
        node.setLat(rs.getDouble("lat"));
        node.setLon(rs.getDouble("lon"));
        return node;
    }

    default XMLGregorianCalendar convertLocalDataTimeToXmlGCal(Timestamp timestamp) throws Exception {
        LocalDateTime ldt = timestamp.toLocalDateTime();
        XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
        cal.setYear(ldt.getYear());
        cal.setMonth(ldt.getMonthValue());
        cal.setDay(ldt.getDayOfMonth());
        cal.setHour(ldt.getHour());
        cal.setMinute(ldt.getMinute());
        cal.setSecond(ldt.getSecond());
        cal.setFractionalSecond(new BigDecimal("0." + ldt.getNano()));
        return cal;
    }

    /*https://stackoverflow.com/questions/7296846/how-to-implement-one-to-one-one-to-many-and-many-to-many-relationships-while-de*/
    /*https://www.postgresqltutorial.com/postgresql-primary-key/*/
    /*http://zetcode.com/java/postgresql/*/
    /*https://www.postgresqltutorial.com/postgresql-create-schema/*/
    /*https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html*/
}
