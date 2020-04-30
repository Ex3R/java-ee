package ru.nsu.g16207.terekhov.lab3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.g16207.terekhov.lab3.domain.NodeEntity;

import java.util.Optional;

@Repository
public interface NodeEntityRepository extends CrudRepository<NodeEntity, Long> {

    Optional<NodeEntity> findByNodeId(Long nodeId);

    Optional<NodeEntity> findById(Integer id);

    Page<NodeEntity> findAllBy(Pageable pageable);


    /*@Query(value = "SELECT n FROM NodeEntity n WHERE FUNCTION('earth_distance', FUNCTION('ll_to_earth',:positionLatitude, :positionLongitude), :radius) > FUNCTION('ll_to_earth',n.lat,n.lon)", nativeQuery = true)*/
    @Query(value = "" +
            "SELECT * " +
            "FROM nodes " +
            "WHERE earth_distance( " +
            "   ll_to_earth(nodes.lat, nodes.lon), " +
            "   ll_to_earth(:latitude, :longitude) " +
            ") < :radius", nativeQuery = true)
    //https://stackoverflow.com/questions/49680761/query-function-ll-to-earthdouble-precision-double-precision-does-not-exist
    //https://serverfault.com/questions/346568/how-do-i-install-the-earthdistance-and-requirement-cube-modules-for-postgresql
    Page<NodeEntity> findAllNodesByLocationAndRadius(@Param("latitude") float latitude,
                                                     @Param("longitude") float longitude,
                                                     @Param("radius") int radius,
                                                     Pageable pageable);
}
