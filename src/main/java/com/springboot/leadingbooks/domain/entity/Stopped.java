package com.springboot.leadingbooks.domain.entity;

import com.springboot.leadingbooks.domain.enum_.Period;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
