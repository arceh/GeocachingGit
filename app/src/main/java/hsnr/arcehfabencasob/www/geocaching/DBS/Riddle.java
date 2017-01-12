package hsnr.arcehfabencasob.www.geocaching.DBS;



import android.support.v4.media.RatingCompat;

import java.util.HashMap;

import hsnr.arcehfabencasob.www.geocaching.GlobaleKoordinaten.Coordinate;

/**
 * Created by carsten on 03.11.16.
 */

public class Riddle {

    private String riddleName;
    private HashMap<Integer, Question> questions;
    private float rating;
    private String creatorName;
    private int id;
    private int ratingcount;

    public Riddle() {
        this.riddleName = null;
        this.questions = null;
        this.rating = 0;
        this.creatorName=null;
        this.id = 0;
        this.ratingcount = 0;
    }

    public Riddle(String riddleName, HashMap<Integer, Question> questions, String creatorName, int id) {
        this.riddleName = riddleName;
        this.questions = questions;
        this.rating = 0;
        this.creatorName = creatorName;
        this.id = id;
        this.ratingcount = 0;
    }

    public Riddle(String riddleName, HashMap<Integer, Question> questions, String creatorName, int id, float rating, int ratingcount) {
        this.riddleName = riddleName;
        this.questions = questions;
        this.rating = rating;
        this.creatorName = creatorName;
        this.id = id;
        this.ratingcount = ratingcount;
    }

    public Riddle(String riddleName, HashMap<Integer, Question> questions, String creatorName, float rating, int ratingcount) {
        this.riddleName = riddleName;
        this.questions = questions;
        this.rating = rating;
        this.creatorName = creatorName;
        this.ratingcount = ratingcount;
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

    public int getRatingCount() {
        return this.ratingcount;
    }

    public void setRatingCount(int ratingcount) {
        this.ratingcount = ratingcount;
        return;
    }


}
