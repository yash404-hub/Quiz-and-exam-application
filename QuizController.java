package controllers;

import dao.QuizDAO;
import models.Quiz;
import java.util.List;

public class QuizController {
    private QuizDAO quizDAO;

    public QuizController() {
        quizDAO = new QuizDAO();
    }

    public List<Quiz> getAllQuestions() {
        return quizDAO.getAllQuizzes();
    }
}
