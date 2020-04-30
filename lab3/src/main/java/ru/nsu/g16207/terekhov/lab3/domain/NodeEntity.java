package ru.nsu.g16207.terekhov.lab3.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "nodes")
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "nodeId")
public class NodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "id")
    private Integer id;

    @Column(name = "version")
    private Integer version;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "uid")
    private Integer uid;

    @Column(name = "\"user\"")
    private String user;

    @Column(name = "changeset")
    private Integer changeset;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @JsonBackReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "nodeEntity", orphanRemoval = true)
    private List<TagEntity> tags = new ArrayList<>();

    public static NodeEntity of(Node node) {
        Timestamp timestamp = new Timestamp(node.getTimestamp().toGregorianCalendar().getTimeInMillis());
        List<Node.Tag> tags = node.getTag();
        List<TagEntity> tagEntityList = tags.stream().map(TagEntity::of).collect(Collectors.toList());
        NodeEntity nodeEntity = new NodeEntity(
                node.getId(),
                node.getVersion(),
                timestamp,
                node.getUid(),
                node.getUser(),
                node.getChangeset(),
                node.getLat(),
                node.getLon());
        nodeEntity.setTags(tagEntityList);
        return nodeEntity;
    }

    public NodeEntity(Integer id, Integer version, Timestamp timestamp, Integer uid, String user, Integer changeset, Double lat, Double lon, List<TagEntity> tags) {
        this.id = id;
        this.version = version;
        this.timestamp = timestamp;
        this.uid = uid;
        this.user = user;
        this.changeset = changeset;
        this.lat = lat;
        this.lon = lon;
        this.tags = tags;
    }

    public NodeEntity(Integer id, Integer version, Timestamp timestamp, Integer uid, String user, Integer changeset, Double lat, Double lon) {
        this.id = id;
        this.version = version;
        this.timestamp = timestamp;
        this.uid = uid;
        this.user = user;
        this.changeset = changeset;
        this.lat = lat;
        this.lon = lon;
    }

    public List<TagEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagEntity> tags) {
        for (TagEntity tag : tags) {
            // initializing the TestObj instance in Children class (Owner side)
            // so that it is not a null and PK can be created
            tag.setupNodeEntity(this);
        }
        this.tags = tags;
    }

    public void addTag(TagEntity tagEntity) {
        if (tags.contains(tagEntity)) {
            return;
        }

        tags.add(tagEntity);
        tagEntity.setNodeEntity(this);
    }

    public void removeTag(TagEntity tagEntity) {
        if (!tags.contains(tagEntity)) {
            return;
        }

        tags.remove(tagEntity);
        tagEntity.setupNodeEntity(null);
    }


    @Override
    public String toString() {
        return "NodeEntity{" +
                "nodeId=" + nodeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeEntity)) return false;
        NodeEntity nodeEntity = (NodeEntity) o;
        return getNodeId().equals(nodeEntity.getNodeId()) &&
                getId().equals(nodeEntity.getId()) &&
                getVersion().equals(nodeEntity.getVersion()) &&
                getTimestamp().equals(nodeEntity.getTimestamp()) &&
                getUid().equals(nodeEntity.getUid()) &&
                getUser().equals(nodeEntity.getUser()) &&
                getChangeset().equals(nodeEntity.getChangeset()) &&
                getLat().equals(nodeEntity.getLat()) &&
                getLon().equals(nodeEntity.getLon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNodeId(), getId(), getVersion(), getTimestamp(), getUid(), getUser(), getChangeset(), getLat(), getLon());
    }
}
