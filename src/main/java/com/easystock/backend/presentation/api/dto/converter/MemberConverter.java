package com.easystock.backend.presentation.api.dto.converter;


import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.LoginMemberResponse;
import com.easystock.backend.presentation.api.dto.response.TokenResponse;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component @Builder
public class MemberConverter {
    public static Member toMember(CreateMemberRequest input, String password) {
        return Member.builder()
                .nickname(input.nickname)
                .username(input.username)
                .password(password)
                .birthDate(input.birthDate)
                .isAgreed(input.isAgreed)
                .level(LevelType.ZERO)
                .profileImage(1)
                .isQuizCompleted(false)
                .isTutorialCompleted(false)
                .xpGuage(0)
                .tokenBudget(0)
                .build();
    }

    public static CreateMemberResponse toJoinResDto(Member member) {
        CreateMemberResponse.MemberInfo memberInfo = CreateMemberResponse.MemberInfo.builder()
                .username(member.getUsername())
                .nickname(member.getNickname())
                .build();

        return CreateMemberResponse.builder()
                .memberId(member.getId())
                .memberInfo(memberInfo)
                .build();
    }

    public static LoginMemberResponse toLoginResDto(
            String accessToken,
            String refreshToken,
            Long memberId){

        TokenResponse tokenInfo = TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return LoginMemberResponse.builder()
                .memberId(memberId)
                .tokenResponse(tokenInfo)
                .build();
    }

    public static GetMemberProfileResponse toProfileResDto(Member member){
        return GetMemberProfileResponse.builder()
                .profileImage(member.getProfileImage())
                .nickname(member.getNickname())
                .level(member.getLevel())
                .tokenBudget(member.getTokenBudget())
                .xpGuage(member.getXpGuage())
                .build();
    }
}
