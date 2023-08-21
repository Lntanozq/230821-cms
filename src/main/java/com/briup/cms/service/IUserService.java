package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.User;
import com.briup.cms.bean.extend.UserExtend;

import java.util.List;

public interface IUserService {
    // 新增用户
    void save(User user);

    //设置id为vip
    void setVip(Long id);

    //更新用户信息
    void update(User user);

    //删除指定用户
    void deleteByBatch(List ids);

    //用户登录
    User login(String username, String password);

    //IUserService接口中添加：
    // 根据id查找用户
    User queryById(Long id);

    // 用户查询
    public IPage<UserExtend> query(Integer pageNum, Integer pageSize, String username, String status, Integer roleId, Integer isVip);

}
