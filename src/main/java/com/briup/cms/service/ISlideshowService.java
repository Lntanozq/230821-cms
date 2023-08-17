package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Slideshow;

import java.util.List;

public interface ISlideshowService {

    void deleteById(Integer id);

    void deleteInBatch(List<Integer> ids);

    void saveOrUpdate(Slideshow slideshow);

    List<Slideshow> queryAllEnable();

    IPage<Slideshow> query(Integer page, Integer pageSize, String status, String desc);
}
