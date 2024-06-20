package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
public class Member extends BaseTimeEntity implements UserDetails {
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

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Stopped stopped;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
