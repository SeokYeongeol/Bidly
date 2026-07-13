package com.example.bidly.domain.member.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.member.role.MemberRole;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    @Transactional
    public void changeRole(Auth auth, Long memberId) {
        Member findAdmin = memberRepository.findMemberById(auth.getId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        if (!findAdmin.getRole().equals(MemberRole.ROLE_ADMIN)) {
            throw new ServerException(DO_NOT_HAVE_PERMISSION);
        }

        Member findMember = memberRepository.findMemberById(memberId)
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        if (findMember.getRole().equals(MemberRole.ROLE_MEMBER)) {
            findMember.changeRole(MemberRole.ROLE_ADMIN);
        } else {
            findMember.changeRole(MemberRole.ROLE_MEMBER);
        }
        memberRepository.save(findMember);
    }
}
