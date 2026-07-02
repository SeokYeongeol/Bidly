package com.example.bidly.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EmailRequest {

    @NotNull(message = "이메일을 입력해주세요.")
    private String email;
}
