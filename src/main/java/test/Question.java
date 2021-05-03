package test;

import java.util.ArrayList;

public class Question {

    int Id;
    String question;
    int QuizId;


    public Question(String question) {
        this.question = question;
    }

    public Question(int Id, String question, int QuizId) {
        this.Id = Id;
        this.question = question;
        this.QuizId = QuizId;
    }

    @Override
    public String toString() {
        return "Question{" +
                "Id=" + Id +
                ", question='" + question + '\'' +
                ", TestId=" + QuizId +
                '}';
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTestId() {
        return QuizId;
    }

    public void setTestId(int testId) {
        QuizId = testId;
    }

}
