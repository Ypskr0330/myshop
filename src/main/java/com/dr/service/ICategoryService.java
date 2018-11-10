package com.dr.service;

import com.dr.common.ServerResponse;

public interface ICategoryService {

    /**
     * 查询子类节点
     * */
    ServerResponse get_category(Integer categoryId);
    /**
     * 增加节点
     * */
    ServerResponse add_category(Integer parentId, String categoryName);

    /**
     * 修改类别
     * */
    ServerResponse set_category_name(Integer categoryId, String categoryName);
    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    ServerResponse get_deep_category(Integer categoryId);

}
