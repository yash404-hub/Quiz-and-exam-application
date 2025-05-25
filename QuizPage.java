package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import utils.DatabaseConnection;

public class QuizPage {
    private JFrame frame;
    private JLabel questionLabel;
    private JRadioButton option1, option2, option3, option4;
    private JButton nextButton;
    private ButtonGroup optionsGroup;
    private ResultSet rs;
    private int score = 0;  // Track user score

    public QuizPage() {
        frame = new JFrame("Quiz");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 1));

        questionLabel = new JLabel();
        frame.add(questionLabel);

        option1 = new JRadioButton();
        option2 = new JRadioButton();
        option3 = new JRadioButton();
        option4 = new JRadioButton();

        optionsGroup = new ButtonGroup();
        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);

        frame.add(option1);
        frame.add(option2);
        frame.add(option3);
        frame.add(option4);

        nextButton = new JButton("Next");
        frame.add(nextButton);

        loadQuestions(); // Load questions from database

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rs != null) {
                    try {
                        checkAnswer(); // Validate answer before moving to next
                        if (rs.next()) {
                            updateQuestion(rs);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Quiz Completed! Your Score: " + score);
                            frame.dispose(); // Close window
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        frame.setVisible(true);
    }

    private void loadQuestions() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM quizzes";
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery();
            if (rs.next()) {
                updateQuestion(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateQuestion(ResultSet rs) throws Exception {
        questionLabel.setText(rs.getString("question"));
        option1.setText(rs.getString("option1"));
        option2.setText(rs.getString("option2"));
        option3.setText(rs.getString("option3"));
        option4.setText(rs.getString("option4"));
    }

    private void checkAnswer() throws Exception {
        String correctAnswer = rs.getString("correct_answer"); // Retrieve correct answer from DB
        JRadioButton selectedOption = null;

        if (option1.isSelected()) selectedOption = option1;
        if (option2.isSelected()) selectedOption = option2;
        if (option3.isSelected()) selectedOption = option3;
        if (option4.isSelected()) selectedOption = option4;

        if (selectedOption != null) {
            if (selectedOption.getText().equals(correctAnswer)) {
                score++; // Increase score for correct answer
                JOptionPane.showMessageDialog(frame, "✅ Correct!");
            } else {
                JOptionPane.showMessageDialog(frame, "❌ Incorrect! Correct answer: " + correctAnswer);
            }
        }
    }
}