package com.springboot.security.common;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CommonResponse {
    SUCCESS(0, "Success") ,
    FAIL(-1, "Fail");

    final int code;
    final String msg;
}
