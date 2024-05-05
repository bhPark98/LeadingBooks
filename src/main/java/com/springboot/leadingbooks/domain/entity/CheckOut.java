package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CheckOut {
    @Id
    @GeneratedValue
    @Column(name = "c_no")
    private Long id;

    @Column(name = "c_date")
    @ColumnDefault("14")
    private int cDate;

    @OneToMany(mappedBy = "checkOut", cascade = CascadeType.ALL)
    private List<Book> bookList = new ArrayList<>();

    @OneToMany(mappedBy = "checkOut", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();

}