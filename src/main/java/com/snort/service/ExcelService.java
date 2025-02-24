package com.snort.service;


import com.snort.entities.Question;
import com.snort.helper.Helper;
import com.snort.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class ExcelService {

    @Autowired
    private QuestionRepository repository;

    public void save(MultipartFile file) {

        try {
            List<Question> questions = Helper.convertExcelToListOfQuestions(file.getInputStream());
            this.repository.saveAll(questions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public List<Question> getAllQuestions() {
        return (List<Question>) this.repository.findAll();
    }
}