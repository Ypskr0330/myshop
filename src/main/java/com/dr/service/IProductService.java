package com.dr.service;

import com.dr.common.ServerResponse;
import com.dr.pojo.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {

    /**
     * 新增或更新产品
     * */
    ServerResponse saveOrUpdate(Product product);

    /**
     * 产品上下架
     * */
    ServerResponse set_sale_status(Integer productId, Integer status);

    /**
     * 后台商品详情
     * */
    ServerResponse detail(Integer productId);

    /**
     * 后台商品列表
     * */
    ServerResponse list(Integer pageNum, Integer pageSize);

    /**
     * 商品查询
     * */
    ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize);

    /**
     * 图片上传
     * */
    ServerResponse upload(MultipartFile file, String path);
    /**
     * 前台商品详情
     * */
    ServerResponse detail_portal(Integer productId);

    /**
     * 产品搜索及动态排序List
     * @param categoryId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @param orderBy //排序字段
     * */
    ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);

}

