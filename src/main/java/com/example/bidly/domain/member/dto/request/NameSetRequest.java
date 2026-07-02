package com.example.bidly.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class NameSetRequest {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;
}
