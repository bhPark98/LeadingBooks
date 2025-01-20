package com.springboot.leadingbooks.services.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenInfoDto {
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
}
