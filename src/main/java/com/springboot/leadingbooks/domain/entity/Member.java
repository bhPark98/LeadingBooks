package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "m_no")
    private Long mNo;

    @Embedded
    private Login loginData;

    @Column(name = "m_role")
    private String mRole;

    @Column(name = "m_banned")
    private boolean mBanned;


}
