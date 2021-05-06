package test;

import Database.ZeroDawnDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void EditQuestion(String NewQuestion) {

        Connection con = ZeroDawnDatabase.GetDbCon();

        if (con == null) {
            System.exit(1);
        }

        try {
            String query = "update question set question = ? where question_id = ?;";
            PreparedStatement stmt = con.prepareCall(query);
            stmt.setString(1, NewQuestion);
            stmt.setInt(2, this.Id);
            stmt.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        
        this.question = NewQuestion;
    }

}
