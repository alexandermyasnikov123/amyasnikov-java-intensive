package net.dunice.intensive.spring_boot.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class QuizEntity {
    public static final int TITLE_MIN_LENGTH = 3;

    public static final int TITLE_MAX_LENGTH = 255;

    public static final int DESCRIPTION_MIN_LENGTH = 10;

    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    public static final int MIN_DIFFICULTY = 1;

    public static final int MAX_DIFFICULTY = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false)
    String title;

    @Column(nullable = false)
    Integer difficulty;

    @Column(nullable = false, length = DESCRIPTION_MAX_LENGTH)
    String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<AnswerEntity> answers;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    Set<QuizImageEntity> images;
}


