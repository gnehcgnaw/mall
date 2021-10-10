package com.beatshadow.mall.thirdparty.repository;

import com.beatshadow.mall.thirdparty.entity.neo4j.Dept;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

/**
 * @author gnehcgnaw
 * @date 10:38
 */
public interface DeptRepository extends Neo4jRepository<Dept,Long> {
    List<Dept> findAllByDeptName(String deptName);
}
