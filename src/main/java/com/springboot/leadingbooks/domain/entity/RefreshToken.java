package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash(value = "token", timeToLive = 60*60*24*3)    // 7일
@NoArgsConstructor
@Getter
@ToString
public class RefreshToken {

    @Id
    private Long id;

    @Indexed    //@Id가 붙은 필드 이외에도 추가적으로 데이터를 조회
    private String accessToken;

    private String refreshToken;

    public RefreshToken(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
