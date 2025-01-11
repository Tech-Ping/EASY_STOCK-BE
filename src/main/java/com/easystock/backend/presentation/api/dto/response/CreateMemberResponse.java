package com.easystock.backend.presentation.api.dto.response;

public class CreateMemberResponse {
    private Long memberId;
    private MemberInfo memberInfo;

    static class MemberInfo{
        private String username;
        private String password;
    }
}
