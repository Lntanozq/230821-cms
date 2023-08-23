package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Article;
import com.briup.cms.bean.extend.ArticleExtend;
import com.briup.cms.bean.vo.ArticleParam;

import java.util.List;

public interface IArticleService {

    void saveOrUpdate(Article article);

    void reviewArticle(Long id, String status);

    void deleteById(Long id);

    void deleteInBatch(List<Long> ids);

    ArticleExtend queryByIdWithComments(Long id);



    ArticleExtend queryByIdWithCommentsAndAuthor(Long id);

    IPage<ArticleExtend> query(ArticleParam articleParam);

    ArticleExtend queryById(Long id);
}
