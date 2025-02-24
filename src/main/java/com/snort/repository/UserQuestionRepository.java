package com.snort.repository;

import com.snort.entities.UserQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestionRepository extends JpaRepository<UserQuestion, Long> {
        List<UserQuestion> findByUserId(int userId);
    Page<UserQuestion> findByUserId(int userId, Pageable pageable);
    List<UserQuestion> findByUserIdAndQuestionId( int userId, Long questionId);
}
