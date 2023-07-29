package com.fascam.boardproject.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/*
* 중복되는 메타데이터 같은 속성값 분리
* @MappedSuperclass 이용
*/

@Getter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    /* 메타 데이터 */
    @CreatedDate // 자동으로 생성일시 넣어줌
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy // 자동으로 생성자 넣어줌 >> 누가 생성했는지는 어떻게 알지? >> JpaConfig에서 설정
    @Column(nullable = false, updatable = false, length = 100) // updatable = false : update 불가!
    private String createdBy; // 생성자

    @LastModifiedDate // 자동으로 수정일시 넣어줌
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy // 자동으로 수정자 넣어줌
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자
}
