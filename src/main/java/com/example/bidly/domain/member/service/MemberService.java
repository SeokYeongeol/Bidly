package com.example.bidly.domain.member.service;

import com.example.bidly.domain.member.dto.request.ChangePasswordRequest;
import com.example.bidly.domain.member.dto.request.DeleteMemberRequest;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(Auth auth, ChangePasswordRequest request) {
        if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new ServerException(PASSWORD_SAME_AS_OLD);
        }
        Member findMember = getMember(auth.getId());
        matchPassword(request.getOldPassword(), findMember.getPassword());

        findMember.changePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public void deleteMember(Auth auth, DeleteMemberRequest request) {
        Member findMember = getMember(auth.getId());
        matchPassword(request.getPassword(), findMember.getPassword());

        findMember.deleteMember();
    }

    /**
     * 해당 멤버 아이디를 통해 멤버 리턴
     * 해당 멤버가 존재하지 않거나, 삭제됐다면 예외처리
     */
    private Member getMember(Long memberId) {
        return memberRepository.findMemberById(memberId)
            .orElseThrow(() -> new ServerException(USER_NOT_FOUND));
    }

    /**
     * 입력한 비밀번호와 해당 멤버의 비밀번호가 같은지 확인하는 메서드
     */
    private void matchPassword(String inputPassword, String memberPassword) {
        if (!passwordEncoder.matches(inputPassword, memberPassword)) {
            throw new ServerException(INVALID_PASSWORD);
        }
    }
}
