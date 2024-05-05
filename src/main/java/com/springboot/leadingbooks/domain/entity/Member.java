package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Role;
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
    private Long id;

    @Embedded
    private Login loginData;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role")
    private Role mRole;

    @Column(name = "m_banned")
    private boolean mBanned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c_no")
    private CheckOut checkOut;

}
