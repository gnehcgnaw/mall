package com.beatshadow.mall.thirdparty.entity.neo4j;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * 部门实体
 * @date   10:26
 * @author gnehcgnaw
 */
@NodeEntity(label = "dept") //标明是一个节点实体
@Data
@Builder
public class Dept {
    @Id //实体主键
    @GeneratedValue //实体属性值自增
    private Long id ;
    @Property(name = "deptName")    //实体属性
    private String deptName;
}
