package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Review;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public Optional<Review> findById(Long rId) {
        return em.createQuery("select r from Review r where r.id = :rId", Review.class)
                .setParameter("rId", rId)
                .getResultStream()
                .findFirst();

    }
}
