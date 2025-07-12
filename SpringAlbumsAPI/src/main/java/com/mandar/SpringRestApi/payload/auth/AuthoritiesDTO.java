package com.mandar.SpringRestApi.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthoritiesDTO {

    @NotBlank
    @Schema(description = "Authorities (add multiple authorities separated with space" , example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String authorities;

}
