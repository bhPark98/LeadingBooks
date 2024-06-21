package com.springboot.leadingbooks.domain.repository;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public Optional<Book> findBookById(Long bId) {
        return em.createQuery("select b from Book b where b.id = :id", Book.class)
                .setParameter("id", bId)
                .getResultStream().findAny();
    }

    public Optional<Book> findBookByName(String bName) { // 책 제목으로 찾기(책이 몇권 있는지)
        return em.createQuery("select b from Book b where b.bName = :bName", Book.class)
                .setParameter("bName", bName)
                .getResultStream().findAny();
    }

    public Optional<Book> findBookByWriter(String bWriter) { // 책 작가로 찾기(책이 몇권 있는지)
        return em.createQuery("select b from Book b where b.bWriter =:bWriter", Book.class)
                .setParameter("bWriter", bWriter)
                .getResultStream().findAny();
    }

    public List<Book> findBookByCategory(int pageNumber, int pageSize, Category bCategory) {   // 책 카테고리로 찾기
        return em.createQuery("select b from Book b where b.bCategory =: bCategory", Book.class)
                .setParameter("bCategory", bCategory)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Book> findAllBooks(int pageNumber, int pageSize) {  // 전체 도서 조회
        return em.createQuery("select b from Book b", Book.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

}