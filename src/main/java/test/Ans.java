package test;

import Database.ZeroDawnDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Ans {

    int ANS_Id;
    int QuizId;
    String Answer;
    ArrayList<String> AnswersCntainer = new ArrayList<String>();



    public Ans(String answer) {
        this.Answer = answer;
    }

    public Ans(int Id, String Answer, int QuizId) {
        this.ANS_Id = Id;
        this.Answer = Answer;
        this.QuizId = QuizId;
    }

    public int getId() {
        return ANS_Id;
    }

    public void setId(int id) {
        ANS_Id = id;
    }

    public String getQuestion() {
        return Answer;
    }

    public void setQuestion(String Answer) {
        this.Answer = Answer;
    }

    public int getTestId() {
        return QuizId;
    }

    public void setTestId(int testId) {
        QuizId = testId;
    }

}
