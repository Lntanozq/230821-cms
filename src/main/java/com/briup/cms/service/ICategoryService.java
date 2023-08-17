package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Category;
import com.briup.cms.bean.Slideshow;
import com.briup.cms.bean.extend.CategoryExtend;

import java.util.Calendar;
import java.util.List;

public interface ICategoryService {

    void insert(Category category);

    void update(Category category);

    void deleteById(Integer id);

    void deleteInBatch(List<Integer> ids);

    IPage<Category> query(Integer page, Integer pageSize, Integer parentId);

    List<CategoryExtend> queryAllParent();
}
