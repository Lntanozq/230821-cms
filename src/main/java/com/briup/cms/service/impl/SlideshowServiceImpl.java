package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Slideshow;
import com.briup.cms.dao.SlideshowDao;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.service.ISlideshowService;
import com.briup.cms.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shaoyb
 * @program: 230308-CMS
 * @description TODO
 * @create 2023/3/7 15:40
 **/
@Slf4j
@Service
public class SlideshowServiceImpl implements ISlideshowService {
	@Autowired
	private SlideshowDao slideshowDao;

	@Override
	public Slideshow queryOneById(Integer id) {
		return slideshowDao.selectById(id);
	}

	@Override
	public void saveOrUpdate(Slideshow slideshow) {
		//1.判断轮播图url是否唯一
		if (slideshow.getUrl() != null) {
			LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
			qw.eq(Slideshow::getUrl, slideshow.getUrl());
			Slideshow s = slideshowDao.selectOne(qw);
			if (s != null)
				throw new ServiceException(ResultCode.SLIDESHOW_URL_EXISTED);

			// 重置图片url更新时间
			slideshow.setUploadTime(LocalDateTime.now());
		}

		if (slideshow.getId() == null) {
			//2.新增操作
			if (slideshow.getStatus() == null)
				slideshow.setStatus("启用");

			slideshowDao.insert(slideshow);
		} else {
			//3.更新操作
			//3.1 判断当前轮播图是否有效
			Slideshow s = slideshowDao.selectById(slideshow.getId());
			if (s == null)
				throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);

			//3.2 更新操作
			slideshowDao.updateById(slideshow);
		}
	}

	@Override
	public List<Slideshow> queryAllEnable() {
		LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
		qw.eq(Slideshow::getStatus, "启用");
		qw.orderByDesc(Slideshow::getUploadTime);
		List<Slideshow> list = slideshowDao.selectList(qw);

		if (list == null || list.size() == 0)
			throw new ServiceException(ResultCode.DATA_NONE);

		return list;
	}

	@Transactional
	@Override
	public IPage<Slideshow> query(Integer page, Integer pageSize, String status, String desc) {
		IPage<Slideshow> p = new Page<>(page, pageSize);
		LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
		qw.eq(StringUtils.hasText(status), Slideshow::getStatus, status)
				.like(StringUtils.hasText(desc), Slideshow::getDescription, desc)
				.orderByDesc(Slideshow::getUploadTime);

		slideshowDao.selectPage(p, qw);

		if (p.getTotal() == 0)
			throw new ServiceException(ResultCode.DATA_NONE);

		return p;
	}

	@Override
	public void deleteInBatch(List<Integer> ids) {
		if (ids == null ||ids.isEmpty()){
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}

		//根据ids查找轮播图
		LambdaQueryWrapper<Slideshow> qw = new LambdaQueryWrapper<>();
		qw.in(Slideshow::getId, ids);
		int len = slideshowDao.selectCount(qw);

		log.info("len:{}",len);

		if (len <= 0) {
			throw new ServiceException(ResultCode.SLIDESHOW_NOT_EXISTED);
		}

		slideshowDao.deleteBatchIds(ids);
	}
}
