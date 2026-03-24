package org.example.ceylontraditionalmedicinecenter.repository;

import org.example.ceylontraditionalmedicinecenter.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsByTitle(String title);
}
