package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.RefreshToken;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@RedisHash
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByAccessToken(String accessToken);
}
