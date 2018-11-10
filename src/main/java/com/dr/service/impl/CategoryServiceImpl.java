package com.dr.service.impl;

import com.dr.common.ServerResponse;
import com.dr.dao.CategoryMapper;
import com.dr.pojo.Category;
import com.dr.service.ICategoryService;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 查询平级节点
     * */
    @Override
    public ServerResponse get_category(Integer categoryId) {
        //1.非空检验
        if (categoryId == null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //2.根据categoryId查询类别
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ServerResponse.serverResponseByError("此节点不存在");
        }
        //3.查询子节点
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if (categoryList == null){
            return ServerResponse.serverResponseByError("此节点没有子节点");
        }
        return ServerResponse.serverResponseBySuccess(categoryList);
    }
    /**
     * 添加节点
     * */
    @Override
    public ServerResponse add_category(Integer parentId, String categoryName) {
        //1.参数非空校验
        if (categoryName == null || categoryName.equals("")){
            return ServerResponse.serverResponseByError("类别名称不能为空");
        }
        //2.添加节点
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(1);
        int result = categoryMapper.insert(category);
        if (result > 0){
            return ServerResponse.serverResponseBySuccess();
        }
        //3.返回结果
        return ServerResponse.serverResponseByError("添加失败");
    }

    /**
     * 修改节点
     * */
    @Override
    public ServerResponse set_category_name(Integer categoryId, String categoryName) {
        //1.参数非空校验
        if (categoryId == null || categoryId.equals("")){
            return ServerResponse.serverResponseByError("类别id不能为空");
        }
        if (categoryName == null || categoryName.equals("")){
            return ServerResponse.serverResponseByError("类别名称不能为空");
        }

        //2.根据categoryId查询
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category == null){
            return ServerResponse.serverResponseByError("此节点不存在");
        }
        //3.修改节点
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKey(category);
        if (result > 0){
            return ServerResponse.serverResponseBySuccess();
        }
        //4.返回结果
        return ServerResponse.serverResponseByError("修改失败");
    }

    /**
     * 获取当前分类id及递归子节点categoryId
     * */
    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        //1.参数非空校验
        if (categoryId == null || categoryId.equals("")){
            return ServerResponse.serverResponseByError("类别Id不能为空");
        }
        //2.查询
        Set<Category> categorySet = Sets.newHashSet();
        categorySet = findAllChildCategory(categorySet,categoryId);

        Set<Integer> integerSet = Sets.newHashSet();
        Iterator<Category> categoryIterator = categorySet.iterator();
        while (categoryIterator.hasNext()){
            Category category = categoryIterator.next();
            integerSet.add(category.getId());
        }
        return ServerResponse.serverResponseBySuccess(integerSet);
    }

    private Set<Category> findAllChildCategory(Set<Category> categorySet, Integer categoryId){
        //.查找本节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category !=null){
            categorySet.add(category);
        }
        //查找categoryId下的子节点
        List<Category> categoryList = categoryMapper.findChildCategory(categoryId);
        if (categoryList!=null && categoryList.size()>0){
            for (Category category1: categoryList) {
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
