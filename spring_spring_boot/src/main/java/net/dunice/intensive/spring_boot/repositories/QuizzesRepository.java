package net.dunice.intensive.spring_boot.repositories;

import net.dunice.intensive.spring_boot.entities.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizzesRepository extends JpaRepository<QuizEntity, Long> {
    List<Long> deleteAllByIdIn(List<Long> ids);

    List<Long> deleteAllEntries();
}
