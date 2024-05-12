package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Book;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final EntityManager em;

    public void save(Book book) {   // 책 정보 저장
        em.persist(book);
    }

    public void update(Book book) { // 책 정보 수정
        em.merge(book);
    }

    public void delete(Book book) { // 책 정보 삭제
        em.remove(book);
    }

    public Optional<Integer> findBookByName(String bName) { // 책 제목으로 찾기(책이 몇권 있는지)
        return em.createQuery("select b.bCount from Book b where b.bName = :bName", Integer.class)
                .setParameter("bName", bName)
                .getResultStream().findAny();
    }

    public Optional<Integer> findBookByWriter(String bWriter) { // 책 작가로 찾기(책이 몇권 있는지)
        return em.createQuery("select b.bCount from Book b where b.bWriter =:bWriter", Integer.class)
                .setParameter("bWriter", bWriter)
                .getResultStream().findAny();
    }


}