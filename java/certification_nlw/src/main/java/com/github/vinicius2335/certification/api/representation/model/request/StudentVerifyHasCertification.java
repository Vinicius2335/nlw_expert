package com.github.vinicius2335.certification.api.representation.model.request;

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
    private String email;
    private String technology;
}
