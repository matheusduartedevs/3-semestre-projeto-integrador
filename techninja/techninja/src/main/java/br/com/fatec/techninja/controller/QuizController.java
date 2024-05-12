package br.com.fatec.techninja.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Question {
    public String difficulty;
    public String theme;
    public String question;
    public List<String> answers;
    @JsonProperty("correctAnswer")
    public int correctAnswerIndex;
}

class QuestionSet {
    public List<Question> questions;
}

@RestController
public class QuizController {

    @GetMapping("/quiz")
    public ResponseEntity<List<Question>> getQuiz(@RequestParam String difficulty, @RequestParam String theme) throws IOException {
        System.out.println("Dificuldade: " + difficulty);
System.out.println("Tema: " + theme);

        ObjectMapper mapper = new ObjectMapper();
        QuestionSet questionSet = mapper.readValue(Paths.get("perguntasBd.json").toFile(), QuestionSet.class);

        List<Question> filteredQuestions = questionSet.questions.stream()
                .filter(q -> q.difficulty.equals(difficulty) && q.theme.equals(theme))
                .collect(Collectors.toList());

        Collections.shuffle(filteredQuestions);

        return ResponseEntity.ok(filteredQuestions.subList(0, Math.min(filteredQuestions.size(), 5)));
    }

}
