package com.beatshadow.mall.thirdparty.repository;

import com.beatshadow.mall.thirdparty.entity.neo4j.bom.Bom;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * @author gnehcgnaw
 * @date 10:38
 */
public interface BomRepository extends Neo4jRepository<Bom,Long> {
    List<Bom> findAllByBomName(String bomName);
}
