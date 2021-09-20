package com.beatshadow.mall.oauth2.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/3/23 10:26
 */
public class Test {
    public static void main(String[] args) {
        List<Menu> list = new ArrayList<>();
        Menu menu1 = new Menu();
        menu1.setId("1");
        menu1.setParentId("0");
        menu1.setName("菜单1");
        list.add(menu1);

        Menu menu2 = new Menu();
        menu2.setId("2");
        menu2.setParentId("0");
        menu2.setName("菜单2");
        list.add(menu2);

        Menu menu3 = new Menu();
        menu3.setId("3");
        menu3.setParentId("1");
        menu3.setName("菜单3");
        list.add(menu3);

        Menu menu4 = new Menu();
        menu4.setId("4");
        menu4.setParentId("3");
        menu4.setName("菜单4");
        list.add(menu4);

        List<Menu> menus = TreeParser.getTreeList("0", list);
        System.out.println(menus);
    }
}