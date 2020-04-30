package ru.nsu.g16207.terekhov.lab3.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.g16207.terekhov.lab3.model.osm.Node;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "node_id", nullable = false)
    private NodeEntity nodeEntity;


    public TagEntity(String key, String value, NodeEntity nodeEntity) {
        this.key = key;
        this.value = value;
        this.nodeEntity = nodeEntity;
    }

    public TagEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static TagEntity of(Node.Tag tag) {
        return new TagEntity(tag.getK(), tag.getV());
    }


    public void setupNodeEntity(NodeEntity nodeEntity) {
        if (sameAsFormer(nodeEntity)) {
            return;
        }

        //set new nodeEntity
        NodeEntity oldNodeEntity = this.nodeEntity;
        this.nodeEntity = nodeEntity;
        //remove from the old node entity
        if (oldNodeEntity != null)
            oldNodeEntity.removeTag(this);
        //set myself into new node entity
        if (nodeEntity != null)
            nodeEntity.addTag(this);
    }


    public void removeNodeEntity(NodeEntity nodeEntity) {
        nodeEntity.removeTag(this);
    }

    private boolean sameAsFormer(NodeEntity newNodeEntity) {
        return Objects.equals(nodeEntity, newNodeEntity);
    }

    @Override
    public String toString() {
        return "TagEntity{" +
                "tagId=" + tagId +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagEntity)) return false;
        TagEntity tagEntity = (TagEntity) o;
        return
                key.equals(tagEntity.key) &&
                        value.equals(tagEntity.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
