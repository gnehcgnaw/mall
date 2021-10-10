package com.beatshadow.mall.thirdparty.entity.neo4j.bom;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * @author gnehcgnaw
 * @date 2021-10-08 14:40
 */
@NodeEntity(label = "bom")
@Data
@Builder
public class Bom {

    @Id
    @GeneratedValue
    private Long id ;

    /**
     * 品名
     */
    @Property(name = "bomName")
    private String bomName;
}
