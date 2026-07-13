package com.example.bidly.domain.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateNameRequest {

    @NotNull(message = "변경하실 이름을 입력해주세요.")
    private String name;
}
