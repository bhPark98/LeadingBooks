package com.springboot.leadingbooks.admin.service.memberservice;

import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.entity.Stopped;
import com.springboot.leadingbooks.domain.enum_.Period;
import com.springboot.leadingbooks.domain.enum_.Role;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.domain.repository.StoppedRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberManagementService {
    private final MemberRepository memberRepository;
    private final StoppedRepository stoppedRepository;

    // 전체 회원 조회
    public List<Member> findAllMembers() {
        return memberRepository.findAllMembers();
    }
    // 단일 회원 조회
    public Member findMemberById(Long mId) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return member;
    }
    // 회원 권한 변경
    public void changeMemberRole(Long mId, Role role) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );
        member.changeRole(role);
        memberRepository.save(member);
    }
    // 회원 정지
    public void punishMember(Long mId, String mReason) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        member.changeBanned();
        memberRepository.save(member);

        Stopped stopped = Stopped.builder()
                .member(member)
                .build();

        int sCount = stopped.getSCount();

        if(sCount == 1) {
            Stopped stopped1 = Stopped.builder()
                    .period(Period.THREE)
                    .sReason(mReason)
                    .member(member)
                    .build();

            stoppedRepository.save(stopped1);
        }
        else if(sCount == 2) {
            Stopped stopped2 = Stopped.builder()
                    .period(Period.SEVEN)
                    .sReason(mReason)
                    .member(member)
                    .build();

            stoppedRepository.save(stopped2);
        }
        else {
            Stopped stopped3 = Stopped.builder()
                    .period(Period.PERMANENT)
                    .sReason(mReason)
                    .member(member)
                    .build();

            stoppedRepository.save(stopped3);
        }
    }


}
