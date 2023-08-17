package com.briup.cms.bean.extend;

import com.briup.cms.bean.Article;
import com.briup.cms.bean.Comment;
import lombok.Data;

import java.util.List;

/**
 * @author shaoyb
 * @program: 230314-cms
 * @description TODO
 * @create 2023/3/19 23:46
 **/
@Data
public class ArticleExtend extends Article {
    private List<Comment> comments;
}
