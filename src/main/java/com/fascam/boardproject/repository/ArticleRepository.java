package com.fascam.boardproject.repository;

import com.fascam.boardproject.domain.Article;
import com.fascam.boardproject.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
* ctrl + shift + t : TDD 만들기
*
* QuerydslPredicateExecutor : 이 Entity 안에 있는 모든 field 대한 기본 검색 기능을 추가해줌
*/
@RepositoryRestResource // Repository를 Rest Api로 노출 시킬건지
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    /*
    * interface라 override가 원래는 불가능하지만 java8++ 부터는 가능해짐
    */
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // 현재 QuerydslPredicateExecutor에 의해 Article에 있는 모든 field에 대한 검색이 열려있는데 우린 그걸 원하지 않음
        // bindings.excludeUnlistedProperties(true) : 모든 field가 아닌 선택한 field만 검색 가능하게끔 하는 기능
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 검색 가능하게 하고싶은 filed 설정
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); // query문이 like '${v}'
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // query문이 '%${v}%'
        
    }
}
