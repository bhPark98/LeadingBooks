package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.CheckOut;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CheckOutRepository {
    private final EntityManager em;

    public void save(CheckOut checkOut) {   // 대여 정보 저장
        em.persist(checkOut);
    }

    public void update(CheckOut checkOut) { // 대여 정보 수정
        em.merge(checkOut);
    }

    public void delete(CheckOut checkOut) { // 대여 정보 삭제
        em.remove(checkOut);
    }

    public List<CheckOut> findAll() {
        return em.createQuery("select c from CheckOut c")
                .getResultList();
    }

    public List<CheckOut> findByMemberId(Long mId) {
        return em.createQuery("select c from CheckOut c where member.id = :mId", CheckOut.class)
                .setParameter("mId", mId)
                .getResultList();
    }
}