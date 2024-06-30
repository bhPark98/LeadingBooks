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
@Table(name = "stopped")
@Entity
public class Stopped extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "s_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private Period period;

    @Column(name = "s_date")
    private int sDate;

    @Column(name = "s_reason")
    private String sReason;

    @Column(name = "s_count")
    @ColumnDefault("1")
    private int sCount;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Stopped(Period period, int sDate, String sReason, Member member, int sCount) {
        this.period = period;
        this.sDate = sDate;
        this.sReason = sReason;
        this.member = member;
        this.sCount = sCount;
    }

    public void changeDate(Period period, int sDate) {
        this.period = period;
        this.sDate = sDate;
    }

    public void decreaseSdate() {
        this.sDate--;
    }

    public void increaseScount() {
        this.sCount++;
    }

    public Stopped createWithMember(Member member) {
        return Stopped.builder().member(member).build();
    }
}
