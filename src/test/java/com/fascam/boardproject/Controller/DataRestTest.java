package com.fascam.boardproject.Controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("Spring Data Rest 통합테스트는 불필요하므로 제외")
@DisplayName("Data REST - API 테스트")
@Transactional // Test에서 작동하는 Transactional은 rollback이라 DB에 영향 안감
@AutoConfigureMockMvc // Test 대상이 아닌 @Service, @Repository가 붙은 객체 모두 메모리에 올림
@SpringBootTest // Integration Test, api를 실행한 결과가 repository까지 모두 실행해 DB에 영향이 갈 수 있음. 그래서 Transactional 어노테이션 넣어주기
public class DataRestTest {
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk()) // 존재 여부 확인
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // content 확인
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticlesCommentFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk()) // 존재 여부 확인
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // content 확인
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk()) // 존재 여부 확인
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // content 확인
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk()) // 존재 여부 확인
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // content 확인
    }
}
