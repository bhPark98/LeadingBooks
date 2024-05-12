package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final EntityManager em;

    public void save(Review review) {   // 리뷰 정보 저장
        em.persist(review);
    }

    public void update(Review review) { // 리뷰 정보 수정
        em.merge(review);
    }

    public void delete(Review review) { // 리뷰 정보 삭제
        em.remove(review);
    }


}
