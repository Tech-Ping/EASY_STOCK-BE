package com.easystock.backend.application.converter;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
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
}
