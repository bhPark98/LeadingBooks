package com.springboot.leadingbooks.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Login {
    @Column(name = "m_name")
    private String mName;

    @Column(name = "m_id")
    private String mId;

    @Column(name = "m_pwd")
    private String mPwd;

    @Column(name = "m_phone")
    private String mPhone;

    @Column(name = "m_email")
    private String mEmail;

    @Column(name = "m_nickname")
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
}
