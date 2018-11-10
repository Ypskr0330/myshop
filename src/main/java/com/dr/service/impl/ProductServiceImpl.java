package com.dr.service.impl;

import com.dr.common.Const;
import com.dr.common.ServerResponse;
import com.dr.dao.CategoryMapper;
import com.dr.dao.ProductMapper;
import com.dr.pojo.Category;
import com.dr.pojo.Product;
import com.dr.service.ICategoryService;
import com.dr.service.IProductService;
import com.dr.utils.DateUtils;
import com.dr.utils.FTPUtil;
import com.dr.utils.PropertiesUtils;
import com.dr.vo.ProductDetailVO;
import com.dr.vo.ProductListVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ICategoryService categoryService;

    /**
     * 新增或更新产品
     * */
    @Override
    public ServerResponse saveOrUpdate(Product product) {
        //1.参数非空校验
        if (product == null){
            return ServerResponse.serverResponseByError("参数为空");
        }
        //2.设置主图片，
        //sub_images（产品子图，多张）-->1.jpg,2.jpg,3.jpg,第一张图作为主图
        String subImages = product.getSubImages();
        if (subImages !=null && !subImages.equals("")){
            //取主图，子图按","分割
            String[] subImagesArr = subImages.split(",");
            if (subImagesArr.length>0){
                product.setMainImage(subImagesArr[0]);
            }
        }
        //3.对商品添加或更新
        if (product.getId() == null){
            //为空说明是添加
            int result = productMapper.insert(product);
            if (result > 0){
                return ServerResponse.serverResponseBySuccess("添加成功");
            }else {
                return ServerResponse.serverResponseBySuccess("添加失败");
            }
        }else{
            //不为空说明是更新
            int result = productMapper.updateByPrimaryKey(product);
            if (result > 0){
                return ServerResponse.serverResponseBySuccess("更新成功");
            }else {
                return ServerResponse.serverResponseBySuccess("更新失败");
            }
        }
        //4.返回结果
    }

    /**
     * 产品上下架，修改商品状态
     * */
    @Override
    public ServerResponse set_sale_status(Integer productId, Integer status) {
        //1.参数非空校验
        if (productId == null){
            return ServerResponse.serverResponseByError("参数为空");
        }
        if (status == null){
            return ServerResponse.serverResponseByError("参数为空");
        }
        //2.更新商品状态
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateProductKeySelective(product);
        if (result >0){
            return ServerResponse.serverResponseBySuccess("修改产品状态成功");
        }
        return ServerResponse.serverResponseByError("修改产品状态失败");
    }
    /**
     * 商品详情
     * */
    @Override
    public ServerResponse detail(Integer productId) {
        //1.参数非空校验
        if (productId == null){
            return ServerResponse.serverResponseByError("参数为空");
        }
        //2.根据商品Id查询商品
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.serverResponseByError("商品不存在");
        }
        //3.结果转为ProductDetailVO
        ProductDetailVO productDetailVO = assemblProductDetailVO(product);
        //4.返回结果
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    private ProductDetailVO assemblProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCatagoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dataToString(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(PropertiesUtils.readByKey("imageHost"));//图片路径
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dataToString(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category !=null){
            productDetailVO.setCatagoryId(category.getParentId());
        }else {
            productDetailVO.setCatagoryId(0);
        }
        return productDetailVO;
    }

    /**
     * 后台-分页查看商品列表
     * */
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        //1.查询商品数据
        //在查询商品之前调用插件，在查询语句后加limit(pageNum-1)*pageSize,pageSize相当于springAOP
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectAll();
        //2.转为VO
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList != null && productList.size()>0){
            for (Product product:productList) {
                ProductListVO productListVO = assmbleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //3.返回结果
        //分页对象
        PageInfo pageInfo = new PageInfo(productListVOList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    /**
     * 商品查询
     * */
    @Override
    public ServerResponse search(Integer productId, String productName, Integer pageNum, Integer pageSize) {
        //select * from product where productId = ? and productName like %name%
        //分页,用分页插件后，SQL语句后不写分号
        PageHelper.startPage(pageNum,pageSize);
        //
        if (productName != null && productName.equals("")){
            productName="%"+productName+"%";
        }
        List<Product> productList = productMapper.findProductByProductIdAndProductName(productId,productName);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        if (productList != null){
            for (Product product:productList) {
                ProductListVO productListVO = assmbleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOList);

        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    private ProductListVO assmbleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setName(product.getName());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }

    /**
     * 图片上传
     * */
    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if (file == null){
            return ServerResponse.serverResponseByError();
        }
        //1.获取图片名称
        String originalFileName = file.getOriginalFilename();
        //2.获取图片扩展名（.jpg......）
        String exName = originalFileName.substring(originalFileName.lastIndexOf("."));
        //3.为图片生成新的唯一名字
        String newFileName = UUID.randomUUID().toString()+exName;
        //4.看存在哪个路径里
        //判断路径是否存在
        File pathFile = new File(path);
        if (!pathFile.exists()){
            pathFile.setWritable(true);//可写
            pathFile.mkdirs();//生成目录
        }

        File file1 = new File(path,newFileName);

        try {
            //把文件写到file1里
            file.transferTo(file1);
            //之后还需上传到图片服务器
            FTPUtil.uploadFile(Lists.<File>newArrayList(file1));
            //前端返回json数据，一个uri，一个url
            Map<String ,String> map = Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url",PropertiesUtils.readByKey("imageHost")+"/"+newFileName);
            //删除应用服务器上的图片
            file1.delete();
            return ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /***
     * 前台商品详情
     * */
    @Override
    public ServerResponse detail_portal(Integer productId) {
        //1.参数非空校验
        if (productId == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.查询product
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null){
            return ServerResponse.serverResponseByError("商品不存在");
        }
        //3.校验商品状态
        if (product.getStatus() != Const.ProductStatusEnum.PRODUCT_ONLINE.getCode()){
            return ServerResponse.serverResponseByError("商品已下架");
        }
        //4.获取productDetailVO
        ProductDetailVO productDetailVO = assemblProductDetailVO(product);
        //5.返回结果
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    /**
     * 产品搜索及动态排序List
     * */
    @Override
    public ServerResponse list(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy) {
        //1.参数校验
        //虽说不是必须，但categoryId和keyword不能同时为空
        if (categoryId == null && (keyword == null || keyword.equals(""))){
            return ServerResponse.serverResponseByError("参数错误");
        }
        Set<Integer> integerSet = Sets.newHashSet();
        //2.根据categoryId查询
        if (categoryId != null){
            Product product = productMapper.selectByPrimaryKey(categoryId);
            if (product == null && (keyword==null && keyword.equals(""))){
                //这是说明没有该数据
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.serverResponseBySuccess(pageInfo);
            }
           ServerResponse serverResponse =  categoryService.get_deep_category(categoryId);
            if (serverResponse.isSuccess()){
                integerSet = (Set<Integer>) serverResponse.getData();
            }
        }
        //3.根据keyword查询
        if (keyword != null && !keyword.equals("")){
            keyword = "%"+keyword+"%";
        }
        if (orderBy.equals("")){
            PageHelper.startPage(pageNum,pageSize);
        }else {
            String[] orderByArr = orderBy.split("_");
            if (orderByArr.length > 1) {
                PageHelper.startPage(pageNum, pageSize, orderByArr[0] + " " + orderByArr[1]);
            } else {
                PageHelper.startPage(pageNum, pageSize);
            }
        }
        List<Product> productList = productMapper.searchProduct(integerSet,keyword);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        //4.转VO
        if (productList != null && productList.size()>0){
            for (Product product: productList) {
                ProductListVO productListVO = assmbleProductListVO(product);
                productListVOList.add(productListVO);
            }
        }
        //5.分页
        PageInfo pageInfo = new PageInfo(productListVOList);
        //6.返回结果
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
