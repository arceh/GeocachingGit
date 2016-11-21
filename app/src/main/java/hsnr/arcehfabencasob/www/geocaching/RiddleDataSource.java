package hsnr.arcehfabencasob.www.geocaching;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by carsten on 03.11.16.
 */

public class RiddleDataSource {

    private static final String LOG_TAG = RiddleDataSource.class.getSimpleName();
    private SQLiteDatabase database;
    private RiddleDbHelper dbHelper;

    private String[] columns_all_riddles = {
            RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID,
            RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME,
            RiddleDbHelper.TABLE_RIDDLES_CREATORNAME,
            RiddleDbHelper.TABLE_RIDDLES_TARGET_COORD,
            RiddleDbHelper.TABLE_RIDDLES_COUNT_QUESTIONS,
            RiddleDbHelper.TABLE_RIDDLES_RATING
    };

    private String[] columns_all_riddles_specific = {
            RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ID,
            RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_QUESTIONNUMBER,
            RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_QUESTION,
            RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ANSWER
    };

    public RiddleDataSource(Context context) {
        Log.d(LOG_TAG, "Die DataSource erzeugt den DbHelper");
        dbHelper = new RiddleDbHelper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank wurde mit Hilfe des DbHelpers geschlossen.");
    }

    public Riddle setRiddleInDatabase(Riddle r) {
        ContentValues values_all_riddles = new ContentValues();
        values_all_riddles.put(RiddleDbHelper.TABLE_RIDDLES_CREATORNAME, r.getCreatorName());
        values_all_riddles.put(RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME, r.getRiddleName());
        values_all_riddles.put(RiddleDbHelper.TABLE_RIDDLES_RATING, r.getRating());
        values_all_riddles.put(RiddleDbHelper.TABLE_RIDDLES_COUNT_QUESTIONS, r.getQuestionCount());
        values_all_riddles.put(RiddleDbHelper.TABLE_RIDDLES_TARGET_COORD, r.getTargetCoord());

        long insertId = database.insert(RiddleDbHelper.TABLE_RIDDLES, null, values_all_riddles);

        ContentValues values_riddle_specific = new ContentValues();

        for(int i = 0; i < r.getQuestionCount(); i++) {
            values_riddle_specific.put(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_QUESTIONNUMBER, i + 1);
            values_riddle_specific.put(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_QUESTION, r.getQuestions().get(i + 1).getQuestion());
            values_riddle_specific.put(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ANSWER, r.getQuestions().get(i + 1).getAnswer().toString());
            values_riddle_specific.put(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ID, insertId);
            database.insert(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC, null, values_riddle_specific);
        }


        Cursor cursor_all_riddles = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID + "=" + insertId, null, null, null, null);
        //Cursor cursor_riddle_specific = database.query(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC, columns_all_riddles_specific, RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ID + "=" + insertId, null, null, null, null);
        cursor_all_riddles.moveToFirst();
        Riddle riddle = cursorToRiddle(cursor_all_riddles, CursorToRiddleMode.SINGLE);
        cursor_all_riddles.close();
        return riddle;
    }

    private Riddle cursorToRiddle(Cursor cursor_Ar, int mode) {
        if(!cursor_Ar.isFirst() && mode == CursorToRiddleMode.SINGLE) {
            cursor_Ar.moveToFirst();
        }
        int ColumnId = cursor_Ar.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID);
        int RiddlenameId = cursor_Ar.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME);
        int CreatornameId = cursor_Ar.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_CREATORNAME);
        int RatingId = cursor_Ar.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_RATING);

        int RiddleId = cursor_Ar.getInt(ColumnId);

        Cursor question_cursor = database.query(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC, columns_all_riddles_specific, RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ID + "=" + RiddleId, null, null, null, null);
        HashMap<Integer, Question> RiddleQuestions = new HashMap<Integer, Question>();
        question_cursor.moveToFirst();
        int questioncount = 0;
        do {
            questioncount++;
            try {
                RiddleQuestions.put(questioncount, new Question(question_cursor.getString(question_cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_QUESTION)), new Coordinate(question_cursor.getString(question_cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_SPECIFIC_ANSWER)))));
            }
            catch(Exception ex) {
                Log.e(LOG_TAG, ex.getMessage());
            }

        } while(question_cursor.moveToNext());

        String Riddlename = cursor_Ar.getString(RiddlenameId);
        String RiddleCreatorname = cursor_Ar.getString(CreatornameId);
        float Riddlerating = cursor_Ar.getFloat(RatingId);
        return new Riddle(Riddlename, RiddleQuestions, RiddleCreatorname, RiddleId, Riddlerating);
    }

    public ArrayList<Riddle> getAllRiddles() {
        ArrayList<Riddle> All_Riddles = new ArrayList<Riddle>();
        Cursor cursor_Ar = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, null, null, null, null, null);
        cursor_Ar.moveToFirst();
        do {
            All_Riddles.add(cursorToRiddle(cursor_Ar, CursorToRiddleMode.MULTIPLE));
        } while(cursor_Ar.moveToNext());
        return All_Riddles;
    }

    public ArrayList<Riddle> getAllRiddles(String Order) {
        ArrayList<Riddle> All_Riddles = new ArrayList<Riddle>();
        Cursor cursor_Ar = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, null, null, null, Order, null);
        do {
            All_Riddles.add(cursorToRiddle(cursor_Ar, CursorToRiddleMode.MULTIPLE));
        } while(cursor_Ar.moveToNext());
        return All_Riddles;
    }


    public Riddle getRiddleById(int Id) {
        try {
            Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID + "=" + Id, null, null, null, null);
            return cursorToRiddle(cursor, CursorToRiddleMode.SINGLE);
        } catch( CursorIndexOutOfBoundsException ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return null;
        }
    }

    public ArrayList<Riddle> getRiddlesByName(String Riddlename) {
        ArrayList<Riddle> RiddlesByName = new ArrayList<Riddle>();
        try {
            Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME + "=\"" + Riddlename + "\"", null, null, null, null);
            cursor.moveToFirst();
            do {
                RiddlesByName.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
            } while (cursor.moveToNext());
            return RiddlesByName;
        } catch(CursorIndexOutOfBoundsException ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return null;
        }
    }

    public ArrayList<Riddle> getRiddlesByName(String Riddlename, String Order) {
        ArrayList<Riddle> RiddlesByName = new ArrayList<Riddle>();
        try {
            Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME + "=\"" + Riddlename + "\"", null, null, null, Order);
            cursor.moveToFirst();
            do {
                RiddlesByName.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
            } while (cursor.moveToNext());
            return RiddlesByName;
        } catch(CursorIndexOutOfBoundsException ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return null;
        }
    }

    public ArrayList<Riddle> getRiddlesByCreator(String CreatorName) {
        ArrayList<Riddle> Riddles = new ArrayList<Riddle>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_CREATORNAME + "=" + CreatorName, null, null, null, null);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }

    public ArrayList<Riddle> getRiddlesByCreator(String CreatorName, String Order) {
        ArrayList<Riddle> Riddles = new ArrayList<Riddle>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_CREATORNAME + "=" + CreatorName, null, null, Order, null);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }
}
