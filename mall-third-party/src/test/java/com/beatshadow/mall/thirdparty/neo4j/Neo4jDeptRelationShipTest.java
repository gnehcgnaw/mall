package com.beatshadow.mall.thirdparty.neo4j;

import com.beatshadow.mall.thirdparty.entity.neo4j.Dept;
import com.beatshadow.mall.thirdparty.entity.neo4j.RelationShip;
import com.beatshadow.mall.thirdparty.repository.DeptRepository;
import com.beatshadow.mall.thirdparty.repository.RelationShipRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author gnehcgnaw
 * @date 10:40
 */
@SpringBootTest
public class Neo4jDeptRelationShipTest {
    @Autowired
    private RelationShipRepository relationShipRepository;
    @Autowired
    private DeptRepository deptRepository;

    /**
     * CEO
     * -设计部
     * - 设计1组
     * -
     * - 设计2组
     * -技术部
     * - 前端技术部
     * - 后端技术部
     * - 测试技术部
     */
    @Test
    public void testInitCreate() {
        Dept CEO = Dept.builder().deptName("CEO").build();
        Dept dept1 = Dept.builder().deptName("设计部").build();
        Dept dept11 = Dept.builder().deptName("设计1组").build();
        Dept dept12 = Dept.builder().deptName("设计2组").build();

        Dept dept2 = Dept.builder().deptName("技术部").build();
        Dept dept21 = Dept.builder().deptName("前端技术部").build();
        Dept dept22 = Dept.builder().deptName("后端技术部").build();
        Dept dept23 = Dept.builder().deptName("测试技术部").build();
        List<Dept> depts = new ArrayList<>(Arrays.asList(CEO, dept1, dept11, dept12, dept2, dept21, dept22, dept23));
        deptRepository.saveAll(depts);

        RelationShip relationShip1 = RelationShip.builder().parent(CEO).child(dept1).build();
        RelationShip relationShip2 = RelationShip.builder().parent(CEO).child(dept2).build();
        RelationShip relationShip3 = RelationShip.builder().parent(dept1).child(dept11).build();
        RelationShip relationShip4 = RelationShip.builder().parent(dept1).child(dept12).build();
        RelationShip relationShip5 = RelationShip.builder().parent(dept2).child(dept21).build();
        RelationShip relationShip6 = RelationShip.builder().parent(dept2).child(dept22).build();
        RelationShip relationShip7 = RelationShip.builder().parent(dept2).child(dept23).build();
        List<RelationShip> relationShips = new ArrayList<>(Arrays.asList(relationShip1, relationShip2, relationShip3, relationShip4, relationShip5
                , relationShip6, relationShip7));
        relationShipRepository.saveAll(relationShips);
    }

    @Test
    public void testCreate() {
        Dept dept111 = Dept.builder().deptName("设计1组1排").build();
        Dept dept112 = Dept.builder().deptName("设计1组2排").build();
        deptRepository.saveAll(Arrays.asList(dept111, dept112));

        Dept dept11 = deptRepository.findAllByDeptName("设计1组").stream().findFirst().get();
        RelationShip relationShip8 = RelationShip.builder().parent(dept11).child(dept111).build();
        RelationShip relationShip9 = RelationShip.builder().parent(dept111).child(dept112).build();
        RelationShip relationShip10 = RelationShip.builder().parent(dept112).child(dept111).build();
        relationShipRepository.saveAll(Arrays.asList(relationShip8, relationShip9, relationShip10));
    }

    @Test
    public void testQuery() {
        deptRepository.findAllByDeptName("设计部").forEach(System.out::println);
    }


    /**
     * 查询关系，
     * 1、查询全部，
     * 2、根据部门名称查询所属关系，只显示1级
     */
    @Test
    public void testRelationShipQuery() {
        relationShipRepository.findAll().forEach(System.out::println);
        System.out.println(".......");
        relationShipRepository.findAllByParentDeptName("技术部").forEach(System.out::println);
        System.out.println(".......");
        relationShipRepository.findAllByParentDeptName("设计1组").forEach(System.out::println);
    }
}
