package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "m_no")
    private Long id;

    @Embedded
    private Login loginData;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role")
    private Role mRole;

    @Column(name = "m_banned")
    private boolean mBanned;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CheckOut> checkOutList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

}
