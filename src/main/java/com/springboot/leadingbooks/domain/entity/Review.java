package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "r_no")
    private Long id;

    @Column(name = "r_rating")
    private int rRating;

    @Column(name = "r_content")
    private String rContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_no")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_no")
    private Member member;

    @Builder
    public Review(int rRating, String rContent, Member member, Book book) {
        this.rRating = rRating;
        this.rContent = rContent;
        this.member = member;
        this.book = book;
    }

    public void setBook(Book book) {
        if(this.book != null) {
            this.book.getReviewList().remove(this);
        }
        this.book = book;
        if(book != null && !book.getReviewList().contains(this)) {
            book.getReviewList().add(this);
        }
    }
}
