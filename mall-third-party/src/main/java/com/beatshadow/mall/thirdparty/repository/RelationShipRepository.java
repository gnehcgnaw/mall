package com.beatshadow.mall.thirdparty.repository;

import com.beatshadow.mall.thirdparty.entity.neo4j.RelationShip;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * @author gnehcgnaw
 * @date 10:38
 */
public interface RelationShipRepository extends Neo4jRepository<RelationShip,Long> {
    List<RelationShip> findAllByParentDeptName(String deptName);

}
