package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<String> findIdByEmail(String mName, String mEmail) {    // 이메일과 이름으로 아이디찾기
        return em.createQuery("select m.loginData.mId from Member m where m.loginData.mName =:mName and m.loginData.mEmail =:mEmail", String.class)
                .setParameter("mName", mName)
                .setParameter("mEmail", mEmail)
                .getResultStream().findAny();
    }

    public Optional<String> findPwdByEmail(String mId, String mEmail) { // 이메일과 아이디로 비밀번호 찾기
        return em.createQuery("select m.loginData.mPwd from Member m where m.loginData.mId =:mId and m.loginData.mEmail =:mEmail", String.class)
                .setParameter("mId", mId)
                .setParameter("mEmail", mEmail)
                .getResultStream().findAny();
    }

    public Optional<Member> findByName(String mName) {
        return em.createQuery("select m from Member m where m.loginData.mName =:mName", Member.class)
                .setParameter("mName", mName)
                .getResultStream()
                .findFirst();

    }

    public Optional<Member> findById(Long mId) {
        return em.createQuery("select m from Member m where m.loginData.mId =:mId", Member.class)
                .setParameter("mId", mId)
                .getResultStream()
                .findFirst();
    }
}