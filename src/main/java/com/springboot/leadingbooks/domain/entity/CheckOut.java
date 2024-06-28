package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "check_out")
@Entity
public class CheckOut {
    @Id
    @GeneratedValue
    @Column(name = "c_no")
    private Long id;

    @Column(name = "c_date")
    @ColumnDefault("7")
    private int cDate = 7;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b_no")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "m_no")
    private Member member;

    public void setcDate(int cDate) {
        this.cDate = cDate;
    }
}