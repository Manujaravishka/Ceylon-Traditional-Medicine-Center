package org.example.ceylontraditionalmedicinecenter.service;

import org.example.ceylontraditionalmedicinecenter.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {
    int updateArticle(Long id, ArticleDTO articleDTO);

    int saveArticle(ArticleDTO articleDTO);

    int deleteArticle(Long id);

    List<ArticleDTO> getAllArticles();
}
