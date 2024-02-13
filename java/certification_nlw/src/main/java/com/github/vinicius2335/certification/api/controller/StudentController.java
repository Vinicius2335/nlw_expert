package com.github.vinicius2335.certification.api.controller;

import com.github.vinicius2335.certification.api.representation.model.request.StudentCertificationAnswerRequest;
import com.github.vinicius2335.certification.api.representation.model.request.StudentVerifyHasCertification;
import com.github.vinicius2335.certification.domain.model.Certification;
import com.github.vinicius2335.certification.domain.service.CertificationAnswersService;
import com.github.vinicius2335.certification.domain.service.DeleteCertificationService;
import com.github.vinicius2335.certification.domain.service.VerifyIfHasCertificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final VerifyIfHasCertificationService verifyIfHasCertificationService;
    private final CertificationAnswersService certificationAnswersService;
    private final DeleteCertificationService deleteCertificationService;

    /**
     * Endpoint responsável por verificar se o estudante já realizou ou não a certificação
     * @param verifyHasCertification contem o email e uma tecnologia
     * @return uma mensagem
     */
    @PostMapping("/verifyHasCertification")
    public String verifyHasCertification(@RequestBody @Valid StudentVerifyHasCertification verifyHasCertification){
        boolean result = verifyIfHasCertificationService.execute(verifyHasCertification);

        if (result){
            return "Usuário já fez a prova";
        }

        return "Usuário pode fazer a prova";
    }

    /**
     * Endpoint responsável por salvar a prova de certificação do usuário
     * @param request prova de certificação
     * @return uma certificação
     */
    @PostMapping("/certification/answer")
    public ResponseEntity<Certification> certificationAnswer(@RequestBody @Valid StudentCertificationAnswerRequest request) {
        Certification certification = certificationAnswersService.execute(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(certification);
    }

    @DeleteMapping("/certification/{certificationId}")
    public void deleteCertificationService(@PathVariable UUID certificationId){
        deleteCertificationService.execute(certificationId);
    }
}
