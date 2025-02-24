package com.snort.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExamResult {
    private int userId;
    private int score;

    public ExamResult(int userId, int score) {
        this.userId = userId;
        this.score = score;
    }
}