package hsnr.arcehfabencasob.www.geocaching.DBS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by carsten on 03.11.16.
 */

public class RiddleDbHelper extends SQLiteOpenHelper {


    private static final String LOG_TAG = RiddleDbHelper.class.getSimpleName();

    public static final String DB_NAME = "riddles.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_RIDDLES = "all_riddles";
    public static final String TABLE_RIDDLES_SPECIFIC = "all_riddles_specific";

    public static final String TABLE_RIDDLES_COLUMN_ID = "_id";
    public static final String TABLE_RIDDLES_RIDDLENAME = "riddle_name";
    public static final String TABLE_RIDDLES_CREATORNAME = "creator_name";
    public static final String TABLE_RIDDLES_TARGET_COORD = "target_coord";
    public static final String TABLE_RIDDLES_COUNT_QUESTIONS = "question_count";
    public static final String TABLE_RIDDLES_RATING = "rating";


    public static final String TABLE_RIDDLES_SPECIFIC_ID = "_id";
    public static final String TABLE_RIDDLES_SPECIFIC_QUESTIONNUMBER = "question_number";
    public static final String TABLE_RIDDLES_SPECIFIC_QUESTION = "question";
    public static final String TABLE_RIDDLES_SPECIFIC_ANSWER = "answer";

    public static final String SQL_CREATE_TABLE_RIDDLES = "CREATE TABLE " + TABLE_RIDDLES +
            "(" + TABLE_RIDDLES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TABLE_RIDDLES_RIDDLENAME + " TEXT NOT NULL, " +
            TABLE_RIDDLES_CREATORNAME + " TEXT NOT NULL, " +
            TABLE_RIDDLES_TARGET_COORD + " TEXT NOT NULL, " +
            TABLE_RIDDLES_COUNT_QUESTIONS + " INTEGER NOT NULL, " +
            TABLE_RIDDLES_RATING + " REAL);";

    public static final String SQL_CREATE_TABLE_RIDDLES_SPECIFIC = "CREATE TABLE " + TABLE_RIDDLES_SPECIFIC +
            "(" + TABLE_RIDDLES_SPECIFIC_ID + " INTEGER NOT NULL, " +
            TABLE_RIDDLES_SPECIFIC_QUESTIONNUMBER + " INTEGER NOT NULL, " +
            TABLE_RIDDLES_SPECIFIC_QUESTION + " TEXT NOT NULL, " +
            TABLE_RIDDLES_SPECIFIC_ANSWER + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + TABLE_RIDDLES_SPECIFIC_ID + ") REFERENCES " + TABLE_RIDDLES + "(" + TABLE_RIDDLES_COLUMN_ID + "), " +
            "UNIQUE(" + TABLE_RIDDLES_SPECIFIC_ID + "," + TABLE_RIDDLES_SPECIFIC_QUESTIONNUMBER + "));";


    public RiddleDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DBHelper hat die Datenbank" + getDatabaseName() + "erzeugt");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle " + TABLE_RIDDLES + " wird mittels dem SQL-Befehl: " + SQL_CREATE_TABLE_RIDDLES + " angelegt");
            db.execSQL(SQL_CREATE_TABLE_RIDDLES);
            Log.d(LOG_TAG, "Die Tabelle " + TABLE_RIDDLES_SPECIFIC + " wird mittels dem SQL-Befehl: " + SQL_CREATE_TABLE_RIDDLES_SPECIFIC + " angelegt");
            db.execSQL(SQL_CREATE_TABLE_RIDDLES_SPECIFIC);
        } catch(Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
