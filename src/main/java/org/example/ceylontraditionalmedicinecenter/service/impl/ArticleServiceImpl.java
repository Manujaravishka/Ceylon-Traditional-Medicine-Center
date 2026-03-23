package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.ArticleDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Article;
import org.example.ceylontraditionalmedicinecenter.repository.ArticleRepository;
import org.example.ceylontraditionalmedicinecenter.service.ArticleService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int updateArticle(Long id, ArticleDTO articleDTO) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            article.setTitle(articleDTO.getTitle());
            article.setDescription(articleDTO.getDescription());
            if (articleDTO.getImageUrl() != null) {
                article.setImageUrl(articleDTO.getImageUrl());
            }
            articleRepository.save(article);
            return VarList.Created;
        } else {
            return VarList.Not_Found;
        }
    }

    @Override
    public int saveArticle(ArticleDTO articleDTO) {
        if (articleRepository.existsByTitle(articleDTO.getTitle())) {
            return VarList.Not_Acceptable; // Already exists
        }
        Article article = modelMapper.map(articleDTO, Article.class);
        articleRepository.save(article);
        return VarList.Created;
    }

    @Override
    public int deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
            return VarList.Created;
        } else {
            return VarList.Not_Found;
        }
    }

    @Override
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream()
                .map(article -> modelMapper.map(article, ArticleDTO.class))
                .collect(Collectors.toList());
    }
}
