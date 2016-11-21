package hsnr.arcehfabencasob.www.geocaching;

/**
 * Created by carsten on 06.11.16.
 */

public class Question {

    private String question;
    private Coordinate answer;

    public Question(String question, Coordinate answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return this.question;
    }

    public Coordinate getAnswer() {
        return this.answer;
    }

    public void setQuestion(String question) {
        this.question = question; return;
    }

    public void setAnswer(Coordinate answer) {
        this.answer = answer; return;
    }


}
