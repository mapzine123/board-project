package com.fascam.boardproject.repository;

import com.fascam.boardproject.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
* ctrl + shift + t : TDD 만들기
*/
@RepositoryRestResource // Repository를 Rest Api로 노출 시킬건지
public interface ArticleRepository extends JpaRepository<Article, Long> {
}