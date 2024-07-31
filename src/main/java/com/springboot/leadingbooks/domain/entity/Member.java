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
@Builder
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "m_no")
    private Long id;

    @Column(name = "m_pwd",length = 65, nullable = false)
    private String password;

    @Column(name = "m_name", nullable = false)
    private String mName;

    @Column(name = "m_email", length = 100, nullable = false, unique = true)
    private String mEmail;

    @Column(name = "m_nickname", length = 40, nullable = false, unique = true)
    private String mNickname;

    @Column(name = "m_banned")
    @ColumnDefault("false")
    private boolean mBanned;

    @Enumerated(EnumType.STRING)
    @Column(name = "m_role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CheckOut> checkOutList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stopped stopped;

    @Builder
    public Member(String mName, String mEmail, String mNickname, String password) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mNickname = mNickname;
        this.password = password;
    }


    public void changeNickname(String mNickname) {
    this.mNickname = mNickname;
}

    public void changeBanned() {
        if(this.mBanned) {
            this.mBanned = false;
        }
        else {
            this.mBanned = true;
        }
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
