package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member")
@DynamicInsert
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_no")
    private Long id;

    @Embedded
    private Login loginData;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role", nullable = false)
    private Role mRole;

    @Column(name = "m_banned")
    @ColumnDefault("false")
    private boolean mBanned;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckOut> checkOutList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stopped stopped;

    @Builder
    public Member(Login loginData, Role mRole) {
        this.loginData = loginData;
        this.mRole = mRole;
    }

    public void changeBanned() {
        if(this.mBanned) {
            this.mBanned = false;
        }
        else {
            this.mBanned = true;
        }
    }

    public void changeRole(Role mRole) {
        if(this.mRole == Role.ADMIN) {
            this.mRole = Role.USER;
        }else {
            this.mRole = Role.ADMIN;
        }
    }


}
