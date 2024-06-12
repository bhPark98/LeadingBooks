package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Period;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stopped extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "s_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "s_date")
    private Period period;

    @Column(name = "s_reason")
    private String sReason;

    @Column(name = "s_count")
    @ColumnDefault("1")
    private int sCount;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Stopped(Period period, String sReason, Member member) {
        this.period = period;
        this.sReason = sReason;
        this.member = member;
        this.sCount++;
    }

    public Stopped createWithMember(Member member) {
        return Stopped.builder().member(member).build();
    }
}
