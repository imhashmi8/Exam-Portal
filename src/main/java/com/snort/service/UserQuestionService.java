package com.snort.service;

import com.snort.entities.Question;
import com.snort.entities.UserQuestion;
import com.snort.repository.QuestionRepository;
import com.snort.repository.UserQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@Service

public class UserQuestionService {
    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public void assignQuestionsToUser(int userId, String category, String level, Integer setNumber) {
        List<Question> questions = questionRepository.findByCategoryAndLevelAndSetNumber(category, level, setNumber);
        for (Question question : questions) {
            UserQuestion userQuestion = new UserQuestion();
            userQuestion.setUserId(userId);
            userQuestion.setQuestionId(question.getId());
            userQuestionRepository.save(userQuestion);
        }
    }

    /*public List<Question> getAssignedQuestions(Long userId) {
        List<UserQuestion> userQuestions = userQuestionRepository.findByUserId(userId);
        List<Long> questionIds = userQuestions.stream().map(UserQuestion::getQuestionId).collect(Collectors.toList());
        return questionRepository.findAllById(questionIds);
    }*/

    public Page<Question> getAssignedQuestion(int userId, Pageable pageable) {
        Page<UserQuestion> userQuestionPage = userQuestionRepository.findByUserId(userId, pageable);
        if (userQuestionPage.isEmpty()) {
            return Page.empty();
        }
        UserQuestion userQuestion = userQuestionPage.getContent().get(0);
        Question question = questionRepository.findById(userQuestion.getQuestionId())
                .orElseThrow(() -> new NoSuchElementException("Question not found"));
        return new PageImpl<>(Collections.singletonList(question), pageable, userQuestionPage.getTotalElements());
    }

}

