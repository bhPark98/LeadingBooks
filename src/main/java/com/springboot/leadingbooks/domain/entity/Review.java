package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
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

}
