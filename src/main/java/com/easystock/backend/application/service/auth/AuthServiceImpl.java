package com.easystock.backend.application.service.auth;

import com.easystock.backend.application.converter.MemberConverter;
import com.easystock.backend.aspect.exception.JoinException;
import com.easystock.backend.aspect.payload.code.status.ErrorStatus;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public CreateMemberResponse createMember(CreateMemberRequest request){
        Member existingMember = memberRepository.findByUsername(request.username);
        if(existingMember!=null) throw new JoinException(ErrorStatus.JOIN_MEMBER_DUPLICATED);
        if(!request.isAgreed) throw new JoinException(ErrorStatus.JOIN_NOT_AGREED);
        if(!request.password.equals(request.passwordCheck)) throw new JoinException(ErrorStatus.JOIN_PASSWORD_MISMATCH);

        String encodedPassword = passwordEncoder.encode(request.password);
        Member newMember = MemberConverter.toMember(request, encodedPassword);
        memberRepository.save(newMember);
        return MemberConverter.toJoinResDto(newMember);
    }
}
