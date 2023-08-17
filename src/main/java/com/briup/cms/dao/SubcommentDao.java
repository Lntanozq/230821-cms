package com.briup.cms.dao;

import com.briup.cms.bean.Subcomment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.briup.cms.bean.extend.SubCommentExtend;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author briup
 * @since 2023-03-10
 */
public interface SubcommentDao extends BaseMapper<Subcomment> {
    List<SubCommentExtend> queryByParentId(@Param("parentId") Long parentId);
}
