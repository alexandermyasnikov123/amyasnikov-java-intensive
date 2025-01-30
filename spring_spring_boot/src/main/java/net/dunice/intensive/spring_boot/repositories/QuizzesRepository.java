package net.dunice.intensive.spring_boot.repositories;

import net.dunice.intensive.spring_boot.entities.QuizEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface QuizzesRepository extends JpaRepository<QuizEntity, Long> {
    List<Long> deleteAllByIdIn(List<Long> ids);

    @Modifying
    @Query("DELETE FROM QuizEntity")
    List<Long> deleteAllEntries();
}
