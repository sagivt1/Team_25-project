package Test;

import Database.ZeroDawnDatabase;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.Question;
import test.Quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class TestQuiz {

    int Id;
    Quiz quiz;

    @BeforeEach
    void setUp() {

        quiz = new Quiz();
        quiz.setGrade(1);
        quiz.setName("ABC TEST");
        quiz.getQuestions().add(new Question("A"));
        quiz.getQuestions().add(new Question("B"));
        quiz.AddNewQuizToDB();

    }

    @AfterEach
    void tearDown() {
        quiz.RemoveSpecificQuiz(quiz.getId());
    }

    @Test
    void addNewTestToDB() {
        Quiz test = new Quiz();
        test.GetSpecificQuizFromDB(quiz.getId());
        Assert.assertEquals(test.getId(),quiz.getId());
        Assert.assertEquals(test.getGrade(),quiz.getGrade());
        Assert.assertEquals(test.getName(),quiz.getName());
        Assert.assertEquals(test.getQuestions().get(0).getQuestion(),quiz.getQuestions().get(0).getQuestion());
    }

    @Test
    public void RemoveThisQuiz() {
        quiz.RemoveThisQuiz();
        Quiz test = new Quiz();
        test.GetSpecificQuizFromDB(quiz.getId());
        Assert.assertEquals(test.getId(), 0);
    }

    @Test
    public void RemoveSpecificQuiz(){
        quiz.RemoveSpecificQuiz(quiz.getId());
        Quiz test = new Quiz();
        test.GetSpecificQuizFromDB(quiz.getId());
        Assert.assertEquals(test.getId(), 0);
    }

    @Test
    public void UpdateIsActive(){
        quiz.UpdateIsActive();
        Quiz test = new Quiz();
        test.GetSpecificQuizFromDB(quiz.getId());
        Assert.assertFalse(test.isActive());
    }

}