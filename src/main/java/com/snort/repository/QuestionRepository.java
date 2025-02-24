package com.snort.repository;

import com.snort.entities.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
//    List<Question> findByCategoryAndLevel(String category, String level);

    List<Question> findByCategoryAndLevelAndSetNumber(String category,String level, Integer setNumber);

    Integer countByCategoryAndLevelAndSetNumber(String category,String level,Integer setNumber);
    @Query(value = "select sum(total_marks) from question where category=:category and level= :level and set_number=:setNumber",nativeQuery = true)
    Integer sumByCategoryAndLevelAndSetNumber(String category,String level,Integer setNumber);
    Page<Question> findByCategoryAndLevelAndSetNumber(String category, String level, Integer setNumber, Pageable pageable);
}
