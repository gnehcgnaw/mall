package com.beatshadow.mall.thirdparty.entity.neo4j;

import lombok.Builder;
import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * 关系实体
 * @author gnehcgnaw
 * @date 10:30
 */
@RelationshipEntity(type = "relationShip")
@Data
@Builder
public class RelationShip {
    @Id
    @GeneratedValue
    private Long id ;
    @StartNode
    private Dept parent ;
    @EndNode
    private Dept child ;

}
