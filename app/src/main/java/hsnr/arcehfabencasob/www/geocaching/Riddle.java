package hsnr.arcehfabencasob.www.geocaching;



import java.util.HashMap;

/**
 * Created by carsten on 03.11.16.
 */

public class Riddle {

    private String riddleName;
    private HashMap<Integer, Question> questions;
    private float rating;
    private String creatorName;
    private int id;

    public Riddle() {
        this.riddleName = null;
        this.questions = null;
        this.rating = 0;
        this.creatorName=null;
        this.id = 0;
    }

    public Riddle(String riddleName, HashMap<Integer, Question> questions, String creatorName, int id) {
        this.riddleName = riddleName;
        this.questions = questions;
        this.rating = 0;
        this.creatorName = creatorName;
        this.id = id;
    }

    public Riddle(String riddleName, HashMap<Integer, Question> questions, String creatorName, int id, float rating) {
        this.riddleName = riddleName;
        this.questions = questions;
        this.rating = rating;
        this.creatorName = creatorName;
        this.id = id;
    }

    public Riddle(String riddleName, HashMap<Integer, Question> questions, String creatorName, float rating) {
        this.riddleName = riddleName;
        this.questions = questions;
        this.rating = rating;
        this.creatorName = creatorName;
    }

    public String getRiddleName() {
        return this.riddleName;
    }

    public float getRating() {
        return this.rating;
    }

    public int getId() {
        return this.id;
    }

    public HashMap<Integer, Question> getQuestions() {
        return this.questions;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    public int getQuestionCount() {
        return this.questions.size();
    }

    public String getTargetCoord() {
        Coordinate coord = questions.get(questions.size()).getAnswer();
        return coord.toString();
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
        return;
    }

    public void setRiddleName(String riddleName) {
        this.riddleName = riddleName;
        return;
    }

    public void setRating(float rating) {
        this.rating = rating;
        return;
    }

    public void setQuestions(HashMap<Integer, Question> questions) {
        this.questions = questions;
        return;
    }


}
