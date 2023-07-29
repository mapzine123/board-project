package com.fascam.boardproject.repository;

import com.fascam.boardproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/*
* ctrl + shift + t : TDD 만들기
*/
public interface ArticleRepository extends JpaRepository<Article, Long> {
}