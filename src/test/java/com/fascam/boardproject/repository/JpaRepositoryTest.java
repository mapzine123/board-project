package com.fascam.boardproject.repository;

import com.fascam.boardproject.config.JpaConfig;
import com.fascam.boardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // JpaConfig는 내가 만든거라 임포트 안해주면 Auditing이 안되서 제대로 테스트를 해줄 수 없다.
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    // 생성자 주입 패턴
    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles).isNotNull().hasSize(130);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring"));

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // id가 1인걸 가져오는데, orElseThrow() : 없으면 테스트 종료!
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        Article savedArticle = articleRepository.saveAndFlush(article); // Save와 동시에 Flush
        // articleRepository.flush(); 영속성 컨테스트의 변경 내용을 DB에 반영
        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);

        /*
         * JUnit으로 슬라이스 테스트를 돌리면 메서드 단위로 롤백 트랜젝션이 걸림 (@DataJpaTest 때문)
         * 롤백에 의해 메서드의 동작이 중요하지 않다면 생략이 될 수도 있다.
         * update에서 hashtag를 바꾸는데, 바꾸고 아무것도 하지 않는다면 어차피 롤백되서 돌아갈거라 시스템 상에서 생략되는 것
         */
    }
    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        /*
        * Descript
        * Article이 지워지면 cascade 옵션 때문에 ArticleComment도 같이 지워지는지 확인
        */

        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // id가 1인걸 가져오는데, orElseThrow() : 없으면 테스트 종료!
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentsSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        System.out.println(previousArticleCommentCount);
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
    }
}


