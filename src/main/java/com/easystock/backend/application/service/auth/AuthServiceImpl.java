package com.easystock.backend.application.service.auth;

import com.easystock.backend.application.converter.MemberConverter;
import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.aspect.exception.JoinException;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.request.LoginMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
import com.easystock.backend.presentation.api.dto.response.LoginMemberResponse;
import com.easystock.backend.presentation.token.UserAuthentication;
import com.easystock.backend.presentation.token.jwt.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerator tokenGenerator;
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

    @Override
    public LoginMemberResponse loginMember(LoginMemberRequest request){
        Member member = memberRepository.findByUsername(request.username);
        if(member==null) throw new AuthException(ErrorStatus.MEMBER_NOT_FOUND);

        if(!passwordEncoder.matches(request.password, member.getPassword()))
            throw new AuthException(ErrorStatus.LOGIN_PASSWORD_MISMATCH);
        String accessToken = tokenGenerator.generateAccessToken(member.getId());
        String refreshToken = tokenGenerator.generateRefreshToken(member.getId());
        return MemberConverter.toLoginResDto(accessToken, refreshToken, member.getId());
    }
}
