package com.fascam.boardproject.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter // Lombok, 모든 멤버변수 Getter 설정
@ToString // 쉽게 출력 가능
@Table(indexes = { // index 설정
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
// EntityListeners : 엔티티의 변화를 감지해 엔티티와 매핑된 테이블 데이터를 조작, AuditingListener는 엔티티의 저장, 수정을 감지
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {
    @Id // PK 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AutoIncrement, mysql은 IDENTITY 방식으로 autoIncrement해서 바꿔줘야함
    private Long id;

    @Setter
    @Column(nullable = false) // NotNull, true가 기본값, 생략 가능
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000) // length : 최대 글자 수
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그

    /* 양방향 바인딩
     *  실무에서는 강한 결합이 되기 때문에 안하는 경우도 있음.
     * @OneToMany : 1 : N 매핑
     */
    @ToString.Exclude // 순환 참조의 연쇄 해제 ( Article ToString 확인 > Set<ArticleComment 발견 > ArticleComment ToString 확인 > Article 발견 > Article ToString 확인...
    @OrderBy("id") // 정렬 기준 : id
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // mappedBy를 안하면 article_article_comment 이런식으로 두 엔티티를 더한 이름이 생김
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>(); // 중복을 허용하지 않고 여기서 보겠음

    /* 메타 데이터 */
    @CreatedDate // 자동으로 생성일시 넣어줌
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy // 자동으로 생성자 넣어줌 >> 누가 생성했는지는 어떻게 알지? >> JpaConfig에서 설정
    @Column(nullable = false, length = 100)
    private String createdBy; // 생성자

    @LastModifiedDate // 자동으로 수정일시 넣어줌
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy // 자동으로 수정자 넣어줌
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자

    // hibernate 구현체를 쓰려면 반드시 기본생성자가 필요함
    // 평소엔 오픈하지 않을거라 public으로 할 이유가 없다.
    protected Article() {}

    // 자동생성되는 속성은 빼고 생성자 생성
    // private로 설정 후 Factory 메서드를 통해 제공 ( new 키워드를 쓰지 않고 사용할 수 있게 )
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) { // 팩토리 메서드
        return new Article(title, content, hashtag);
    }


    /*
     * 1. db 내의 entity는 pk가 같으면 같다고 볼 수 있다.
     * 2. 만약 pk값이 null이면 (아직 영속화되지 않았다면) 동등성 검사의 의미가 없다고 간주
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

/*
 * Setter를 전체로 안거는 이유?
 * - id나 생성일시 등 시스템에서 자동으로 세팅해주는 부분에 대해서는 변경 가능성을 주고 싶지 않아서
 *
 * pattern matching?
 * java 14 버전 이후에 equals 시 Atricle article = (Article) o; 부분은 필요없는 코드로 보고 보완함
 * */

