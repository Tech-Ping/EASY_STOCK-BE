package com.easystock.backend.application.service.mypage;

import com.easystock.backend.presentation.api.dto.converter.MemberConverter;
import com.easystock.backend.application.service.mypage.MyPageService;
import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service @AllArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final MemberRepository memberRepository;
    @Override
    public GetMemberProfileResponse getMyProfile(Long memberId){
        return memberRepository.findById(memberId)
                .map(MemberConverter::toProfileResDto)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
    }
}