package net.dunice.intensive.spring_boot.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@Table(indexes = @Index(name = "correct_answer_per_quiz_idx", columnList = "is_right, quiz_entity_id", unique = true))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String answer;

    @Column(nullable = false)
    Boolean isRight;
}
