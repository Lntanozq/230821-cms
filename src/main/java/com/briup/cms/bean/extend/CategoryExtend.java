package com.briup.cms.bean.extend;

import com.briup.cms.bean.Category;
import lombok.Data;

import java.util.List;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/17 1:36
 **/
@Data
public class CategoryExtend extends Category {
    //子栏目
    private List<Category> cates;
}
