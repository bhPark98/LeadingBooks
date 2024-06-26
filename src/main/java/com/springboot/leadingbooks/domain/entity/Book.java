package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book")
@Entity
public class Book extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "b_no")
    private Long id;

    @Column(name = "b_name", nullable = false)
    private String bName;

    @Column(name = "b_writer", nullable = false)
    private String bWriter;

    @Column(name = "b_publish", nullable = false)
    private String bPublish;

    @Enumerated(EnumType.STRING)
    @Column(name = "b_category", nullable = false)
    private Category bCategory;

    @Column(name = "b_count", nullable = false)
    private int bCount = 0;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<CheckOut> checkOutList = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @Builder
    public Book(String bName, String bWriter, String bPublish, Category bCategory, List<Review> review) {
        this.bName = bName;
        this.bWriter = bWriter;
        this.bPublish = bPublish;
        this.bCategory = bCategory;
        this.reviewList = review;
    }

    public void increaseBookCount() {
        this.bCount++;
    }

    public void decreaseBookCount() {
        this.bCount--;
    }

    public void addReview(Review review) {
        this.reviewList.add(review);
        review.setBook(this);
    }

    public void removeReview(Review review) {
        this.reviewList.remove(review);
        review.setBook(null);
    }
}
