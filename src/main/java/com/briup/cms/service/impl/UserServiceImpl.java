package com.briup.cms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.User;
import com.briup.cms.bean.extend.UserExtend;
import com.briup.cms.dao.UserDao;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.service.IUserService;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/14 15:04
 **/
@Service
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserDao userDao;

	// 新增用户：username存在不为空且唯一 password不为空
	@Override
	public void save(User user) {
		//1.判断username、password是否存在且不为空
		String username = user.getUsername();
		String password = user.getPassword();
		//1.1 null值判断
		if (username == null || password == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		//1.2 空值判断 “”、“ ”、“   ”、“\t”
		username = username.trim();
		password = password.trim();
		if ("".equals(username) || "".equals(password))
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		//1.3 修改user中username、password值
		user.setUsername(username);
		user.setPassword(password);

		//2.判断username是否唯一
		LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
		qw.eq(User::getUsername, username);
		User u = userDao.selectOne(qw);
		if (u != null)
			throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);

		//3.补充注册时间
		user.setRegisterTime(LocalDateTime.now());

		//4.新增用户
		userDao.insert(user);
	}

	@Override
	public void setVip(Long id) {
		//1.id判断是否有效
		User user = userDao.selectById(id);
		if (user == null)
			throw new ServiceException(ResultCode.DATA_NONE);

		//2.判断是否已经设置为vip
		if (user.getIsVip() == 1)
			throw new ServiceException(ResultCode.PARAM_IS_INVALID);

		//3.修改用户为vip，同时修改过期时间expires_time
		User u = new User();
		u.setId(id);
		u.setIsVip(1);
		LocalDateTime expiresTime = LocalDateTime.now().plusMonths(1);
		u.setExpiresTime(expiresTime);
		userDao.updateById(u);
	}

	//更新用户信息
	@Override
	public void update(User user) {
		//1.id判断
		Long id = user.getId();
		if (id == null || userDao.selectById(id) == null)
			throw new ServiceException(ResultCode.PARAM_IS_INVALID);

		//2.username空白字符去除【实际开发中 交给前端处理】
		String username = user.getUsername();
		if (username != null) {
			username = username.trim();
			if ("".equals(username))
				throw new ServiceException(ResultCode.PARAM_IS_INVALID);

			// 更新username
			user.setUsername(username);

			//3.用户名唯一判断
			LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
			qw.eq(User::getUsername, username)
					.notIn(User::getId, id);
			if (userDao.selectOne(qw) != null)
				throw new ServiceException(ResultCode.USERNAME_HAS_EXISTED);
		}

		//4.更新用户信息
		userDao.updateById(user);
	}

	@Override
	public void deleteById(Long id) {
		//1.有效参数判断
		if (id == null || userDao.selectById(id) == null)
			throw new ServiceException(ResultCode.PARAM_IS_INVALID);

		//2.删除指定用户
		userDao.deleteById(id);
	}

	@Override
	public User login(String username, String password) {
		//1.条件准备
		LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
		lqw.eq(User::getUsername, username)
				.eq(User::getPassword, password);

		//2.查询校验
		User user = userDao.selectOne(lqw);
		if (user == null)
			throw new ServiceException(ResultCode.USER_LOGIN_ERROR);

		return user;
	}

	@Override
	public User queryById(Long id) {
		//1.有效参数判断
		if (id == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		//2.查找用户
		User user = userDao.selectById(id);
		if (user == null)
			throw new ServiceException(ResultCode.DATA_NONE);

		return user;
	}

	//分页+条件查询
	@Override
	public IPage<UserExtend> query(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip) {
		if (pageNum == null || pageSize == null)
			throw new ServiceException(ResultCode.PARAM_IS_BLANK);

		//关键：程序员自行书写sql语句 完成多表连接查询，如何进行分页处理？
		IPage<UserExtend> page = new Page<>(pageNum, pageSize);
		userDao.queryAllUserWithRole(page, username, status, roleId, isVip);

//        System.out.println(page.getTotal());
//        System.out.println(page.getRecords().size());

		return page;
	}
}
