package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Log;
import com.briup.cms.bean.vo.LogExportParam;
import com.briup.cms.bean.vo.LogParam;
import com.briup.cms.dao.LogDao;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.service.ILogService;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/22 17:34
 **/
@Service
public class LogServiceImpl implements ILogService {

	@Autowired
	private LogDao logDao;

	@Override
	public IPage<Log> query(LogParam param) {
		// 1.参数判断
		if (param == null || param.getPageNum() == null || param.getPageSize() == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		// 2.查询条件准备，按时间倒序
		IPage<Log> page = new Page<>(param.getPageNum(), param.getPageSize());

		//获取查询条件
		LambdaQueryWrapper<Log> wrapper = getQueryWrapper(param.getUsername(), param.getRequestUrl(),
				param.getStartTime(), param.getEndTime());

		// 3.执行分页查询
		logDao.selectPage(page, wrapper);

		return page;
	}

	@Override
	public List<Log> queryForExport(LogExportParam param) {
		//条件判断
		if (param == null) {
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);
		}

		//获取查询条件
		LambdaQueryWrapper<Log> wrapper = getQueryWrapper(param.getUsername(), param.getRequestUrl(),
				param.getStartTime(), param.getEndTime());
		//设置日志导出条数
		wrapper.last(Objects.nonNull(param.getCount()), "limit " + param.getCount());

		return logDao.selectList(wrapper);
	}

	private LambdaQueryWrapper<Log> getQueryWrapper(String username, String url, LocalDateTime startTime, LocalDateTime endTime) {
		LambdaQueryWrapper<Log> wrapper = new LambdaQueryWrapper<>();

		wrapper.eq(StringUtils.hasText(username), Log::getUsername, username)
				.eq(StringUtils.hasText(url), Log::getRequestUrl, url)
				.le(endTime != null, Log::getCreateTime, endTime)
				.ge(startTime != null, Log::getCreateTime, startTime)
				.orderByDesc(Log::getCreateTime);

		return wrapper;
	}
}
