package com.dr.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductDetailVO implements Serializable {

    private Integer id;
    private Integer catagoryId;
    private Integer parentCategoryId;
    private String name;
    private String subtitle;
    private String imageHost;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;
            /*"id": 2,
            "categoryId": 2,
            "parentCategoryId":1,
            "name": "oppo R8",
            "subtitle": "oppo促销进行中",
            "imageHost": "http://img.business.com/",
            "mainImage": "mainimage.jpg",
            "subImages": "[\"business/aa.jpg\",\"business/bb.jpg\",\"business/cc.jpg\",\"business/dd.jpg\",\"business/ee.jpg\"]",
            "detail": "richtext",
            "price": 2999.11,
            "stock": 71,
            "status": 1,
            "createTime": "2016-11-20 14:21:53",
            "updateTime": "2016-11-20 14:21:53"*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCatagoryId() {
        return catagoryId;
    }

    public void setCatagoryId(Integer catagoryId) {
        this.catagoryId = catagoryId;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
