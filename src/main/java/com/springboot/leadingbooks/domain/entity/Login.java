package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Login {

    @Column(name = "m_pwd",length = 65, nullable = false, unique = true)
    private String mPwd;

    @Column(name = "m_phone", length = 11, nullable = false, unique = true)
    private String mPhone;

    @Column(name = "m_email", length = 100, nullable = false, unique = true)
    private String mEmail;

    @Column(name = "m_nickname", length = 40, nullable = false, unique = true)
    private String mNickname;

    @Column(name = "m_name", nullable = false)
    private String mName;

    @Builder
    public Login(String mName, String mEmail, String mNickname, String mPwd, String mPhone) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mNickname = mNickname;
        this.mPwd = mPwd;
        this.mPhone = mPhone;
    }

    public void changeNickname(String mNickname) {
        this.mNickname = mNickname;
    }

}