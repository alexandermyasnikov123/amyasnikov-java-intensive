package net.dunice.intensive.spring_boot.repositories;

import net.dunice.intensive.spring_boot.entities.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizzesRepository extends JpaRepository<QuizEntity, Long> {
}
