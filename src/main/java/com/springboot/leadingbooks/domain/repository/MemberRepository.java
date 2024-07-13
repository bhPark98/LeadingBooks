package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final EntityManager em;

    public void save(Member member) {   // 회원 정보 저장
        em.persist(member);
    }

    public void update(Member member) { // 회원 정보 수정
        em.merge(member);
    }

    public void delete(Member member) { //회원 정보 삭제
        em.remove(member);
    }

    public List<Member> findAllMembers() {
        return em.createQuery("select m from Member m")
                .getResultList();

    }


    public Optional<Member> findById(Long id) {
        return em.createQuery("select m from Member m where m.id =:id", Member.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }

    public boolean isNotExistsNickname(String mNickname) {
        long count = em.createQuery("select count(m) from Member m where m.mNickname = :mNickname", Long.class)
                .setParameter("mNickname", mNickname)
                .getSingleResult();

        return count == 0;
    }

    public Optional<Member> findMemberByEmail(String mEmail) {
        return em.createQuery("select m from Member m where m.mEmail = :mEmail", Member.class)
                .setParameter("mEmail", mEmail)
                .getResultStream()
                .findFirst();
    }

    public Optional<Member> findMemberByNickname(String mNickname) {
        return em.createQuery("select m.mNickname from Member m where m.mNickname = :mNickname", Member.class)
                .setParameter("mNickname", mNickname)
                .getResultStream()
                .findFirst();
    }
}