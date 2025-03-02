package com.easystock.backend.presentation.api.payload.code.status;

import com.easystock.backend.presentation.api.payload.code.BaseErrorCode;
import com.easystock.backend.presentation.api.payload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    /* TOKEN exception */
    TOKEN_INVALID_TYPE(HttpStatus.BAD_REQUEST, "TOKEN001", "유효하지 않은 토큰 유형입니다."),
    TOKEN_INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "TOKEN002", "유효하지 않은 액세스 토큰입니다."),

    /* MEMBER exception */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER001", "해당하는 멤버 정보가 없습니다."),

    /* JOIN exception */
    JOIN_MEMBER_DUPLICATED(HttpStatus.CONFLICT, "JOIN001", "이미 가입한 회원 정보입니다."),
    JOIN_NOT_AGREED(HttpStatus.BAD_REQUEST, "JOIN002", "이용약관에 동의하지 않았습니다. 동의 체크 후에 회원가입을 진행해주세요."),
    JOIN_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "JOIN003", "비밀번호가 일치하지 않습니다. 다시 시도해주세요."),

    /* LOGIN exception */
    LOGIN_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "LOGIN001", "기존 회원의 비밀번호가 일치하지 않습니다. 다시 시도해주세요."),

    /* AUTH exception */
    AUTH_UNKNOWN_MEMBER_ID(HttpStatus.NOT_FOUND, "AUTH001", "사용자 인증 정보에서 회원 ID를 추출할 수 없습니다."),

    /* TUTORIAL exception */
    TUTORIAL_UNSUPPORTED(HttpStatus.BAD_REQUEST, "TUTORIAL001", "지원되지 않는 튜토리얼 레벨입니다."),

    /* QUIZ exception */
    QUIZ_NOT_FOUND(HttpStatus.BAD_REQUEST, "QUIZ001", "해당하는 퀴즈 정보가 없습니다."),

    /* STOCK exception */
    STOCK_NOT_FOUND(HttpStatus.BAD_REQUEST, "STOCK001", "해당하는 주식 정보가 없습니다."),

    /* LEVEL exception */
    LEVELUP_CONDITION_UNSATISFIED(HttpStatus.BAD_REQUEST, "LEVELUP001", "레벨업 조건 미충족으로 레벨업에 실패하였습니다."),

    /* TRADE exception */
    TRADE_NOT_FOUND(HttpStatus.BAD_REQUEST, "TRADE001", "해당하는 주문 정보가 없습니다."),
    TRADE_CANNOT_BE_CANCELLED(HttpStatus.BAD_REQUEST, "TRADE002", "이미 처리된 주문을 취소할 수 없습니다."),
    TRADE_NOT_OWNED_BY_USER(HttpStatus.BAD_REQUEST, "TRADE003", "해당 유저의 주문이 아닙니다."),
    INSUFFICIENT_FUNDS(HttpStatus.BAD_REQUEST, "TRADE004", "유저의 잔액이 부족합니다."),
    INSUFFICIENT_STOCKS(HttpStatus.BAD_REQUEST, "TRADE005", "유저의 주식이 부족합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .build();
    }
    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .build();
    }
}
