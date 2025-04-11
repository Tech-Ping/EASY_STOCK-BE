package com.easystock.backend.util;

import java.util.List;

public abstract class Constants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    public static final String ENCODING_TYPE = "utf-8";
    public static final String MEMBER_ID_KEY_NAME = "memberId";
    public static final String TOKEN_TYPE_KEY_NAME = "tokenType";
    public static final List<String> STOCK_CODES = List.of(
            "005930", "373220", "005380", "000660", "083650");
    public static final List<String> EXCLUDED_PATHS = List.of(
            "/api/auth", "/api/test", "/api/flask", "/swagger-ui/", "/token", "/v3/api-docs"
    );
}
