package com.github.vinicius2335.certification.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(length = 50)
    private String technology;

    @Column(length = 10)
    private int grate;

    @Setter(AccessLevel.NONE)
    @OneToMany(
            mappedBy = "certification",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE },
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<AnswersCertification> answersCertificationList = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void addAnswersCertification(UUID questionId, UUID alternativeId, boolean isCorrect){
        answersCertificationList.add(
                AnswersCertification.builder()
                        .student(this.student)
                        .alternative(new Alternative(alternativeId))
                        .question(Question.builder().id(questionId).build())
                        .isCorrect(isCorrect)
                        .certification(this)
                        .build()
        );
    }
}
