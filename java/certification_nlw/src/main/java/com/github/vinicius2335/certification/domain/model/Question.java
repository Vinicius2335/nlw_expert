package com.github.vinicius2335.certification.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(length = 50)
    private String technology;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Setter(AccessLevel.NONE)
    @OneToMany(
            mappedBy = "question",
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE },
            orphanRemoval = true)
    @JsonIgnore
    private List<Alternative> alternatives = new ArrayList<>();

    public void addAlternative(Alternative alternative){
        alternative.setQuestion(this);
        alternatives.add(alternative);
    }
}
