package com.springboot.leadingbooks.admin.service.memberservice;

import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.entity.Stopped;
import com.springboot.leadingbooks.domain.enum_.Period;
import com.springboot.leadingbooks.domain.enum_.Role;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.domain.repository.StoppedRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberManagementService {
    private final MemberRepository memberRepository;
    private final StoppedRepository stoppedRepository;

    // 전체 회원 조회
    @Transactional
    public List<Member> findAllMembers() {
        return memberRepository.findAllMembers();
    }
    // 단일 회원 조회
    @Transactional
    public Member findMemberById(Long mId) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );
        return member;
    }
//    // 회원 권한 변경
//    @Transactional
//    public void changeMemberRole(Long mId, Role role) {
//        Member member = memberRepository.findById(mId).orElseThrow(
//                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
//        );
//        member.changeRole(role);
//        memberRepository.save(member);
//    }
    // 회원 정지
    @Transactional
    public void punishMember(Long mId) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        member.changeBanned();
        memberRepository.save(member);

        Stopped stopped = member.getStopped();

        if(stopped == null) {
            Stopped stopped1 = Stopped.builder()
                    .period(Period.THREE)
                    .sDate(3)
                    .sReason("대출 기한 초과")
                    .member(member)
                    .sCount(1)
                    .build();

            stoppedRepository.save(stopped1);
        }
        else {
            int sCount = stopped.getSCount();
            if(sCount == 1) {
                stopped.changeDate(Period.SEVEN, 7);
                stopped.increaseScount();
                stoppedRepository.save(stopped);
            }
            else if(sCount == 2) {
                stopped.changeDate(Period.PERMANENT, 0);
                stopped.increaseScount();
                stoppedRepository.save(stopped);
            }
        }
    }


}
