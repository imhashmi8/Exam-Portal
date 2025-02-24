package com.snort.entities;


import com.snort.dto.QuestionRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "question")
public class Question {
    @Id
    private Long id;
    private String questionType;

    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String title;
    private Integer totalMarks;
    private String category;
    private String level;
    private String questionDescription;
    private Integer setNumber;

    private String correctAnswer;
    public Question(QuestionRequest questionRequest) {
    }
}