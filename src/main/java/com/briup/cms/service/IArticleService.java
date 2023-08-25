package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Article;
import com.briup.cms.bean.extend.ArticleExtend;
import com.briup.cms.bean.dto.ArticleParam;

import java.util.List;

public interface IArticleService {

    void saveOrUpdate(Article article);

    void reviewArticle(Long id, String status);

    void deleteInBatch(List<Long> ids);

    ArticleExtend queryByIdWithComments(Long id);



    ArticleExtend queryByIdForCustomer(Long id);

    IPage<ArticleExtend> query(ArticleParam articleParam);

    ArticleExtend queryById(Long id);

	List<Article> list();
}
