package com.easystock.backend.presentation.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateMemberResponse {
    public Long memberId;
    public MemberInfo memberInfo;

    @Builder
    public static class MemberInfo{
        public String username;
        public String nickname;
    }
}
