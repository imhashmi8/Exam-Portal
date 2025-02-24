package com.snort.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private String questionType;
    private Integer totalMarks;
    private String category;
    private String level;
    private String questionDescription;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String title;
}