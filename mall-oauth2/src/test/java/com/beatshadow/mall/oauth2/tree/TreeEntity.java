package com.beatshadow.mall.oauth2.tree;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2021/3/23 10:23
 */

import java.util.List;

/**
 * 树形数据实体接口
 * @param <E>
 * @author jianda
 * @date 2017年5月26日
 */
public interface TreeEntity<E> {
    public String getId();
    public String getParentId();
    public void setChildList(List<E> childList);
}
