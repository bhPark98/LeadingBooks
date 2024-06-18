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
    @Column(name = "m_name")
    private String mName;

    @Column(name = "m_id",length = 40, nullable = false, unique = true)
    private String mId;

    @Column(name = "m_pwd",length = 65, nullable = false)
    private String mPwd;

    @Column(name = "m_phone", length = 11, nullable = false)
    private String mPhone;

    @Column(name = "m_email", length = 100, nullable = false)
    private String mEmail;

    @Column(name = "m_nickname", length = 40, nullable = false)
    private String mNickname;

    @Builder
    public Login(String mName, String mEmail, String mId, String mNickname, String mPwd, String mPhone) {
        this.mName = mName;
        this.mEmail = mEmail;
        this.mId = mId;
        this.mNickname = mNickname;
        this.mPwd = mPwd;
        this.mPhone = mPhone;
    }

    public void changeNickname(String mNickname) {
        this.mNickname = mNickname;
    }

}