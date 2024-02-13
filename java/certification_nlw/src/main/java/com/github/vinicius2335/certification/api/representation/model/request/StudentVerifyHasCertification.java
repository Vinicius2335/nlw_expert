package com.github.vinicius2335.certification.api.representation.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa as informações necessárias para o estudante
 * verificar se já realizou ou não a prova de certificação
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentVerifyHasCertification {
    @Email(message = "Email cannot be invalid")
    @NotBlank(message = "Email cannot be null or empty ")
    private String email;

    @NotBlank(message = "Technology cannot be null or empty")
    private String technology;
}
