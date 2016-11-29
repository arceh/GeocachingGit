package hsnr.arcehfabencasob.www.geocaching.DBS;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.HashMap;

import hsnr.arcehfabencasob.www.geocaching.GlobaleCordinaten.Coordinate;

/**
 * Created by carsten on 03.11.16.
 */

public class RiddleDataSource {

    private static final String LOG_TAG = RiddleDataSource.class.getSimpleName();
    private static SQLiteDatabase database;
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

    private String[] columns_user = {
            RiddleDbHelper.TABLE_USER_ID,
            RiddleDbHelper.TABLE_USER_PASSWORD,
            RiddleDbHelper.TABLE_USER_NAME
    };

    private boolean matchesColumn(String column) {
        if(column == RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID || column == RiddleDbHelper.TABLE_RIDDLES_COUNT_QUESTIONS || column == RiddleDbHelper.TABLE_RIDDLES_CREATORNAME || column == RiddleDbHelper.TABLE_RIDDLES_RATING || column == RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME || column == RiddleDbHelper.TABLE_RIDDLES_TARGET_COORD) {
            return true;
        } else {
            return false;
        }
    }

    public RiddleDataSource(Context context) {
        Log.d(LOG_TAG, "Die DataSource erzeugt den DbHelper");
        dbHelper = new RiddleDbHelper(context);
    }


    /**
     * Öffnet die Datenquelle.
     * */
    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    /**
     * Schließt die Datenquelle.
     * */
    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank wurde mit Hilfe des DbHelpers geschlossen.");
    }

    /**
     * Setzt ein neues Rätsel in die Datenbank.
     * @param r Riddle : Rätsel, welches hinzugefügt werden soll.
     * */
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


    /**
     * Liefert alle Rätsel.
     * */
    public ArrayList<Riddle> getAllRiddles() {
        ArrayList<Riddle> All_Riddles = new ArrayList<Riddle>();
        Cursor cursor_Ar = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, null, null, null, null, null);
        cursor_Ar.moveToFirst();
        do {
            All_Riddles.add(cursorToRiddle(cursor_Ar, CursorToRiddleMode.MULTIPLE));
        } while(cursor_Ar.moveToNext());
        return All_Riddles;
    }


    /**
     * Liefert alle Rätsel in der bestimmten Reihenfolge.
     * @param Order String : Sortierreihenfolge.
     * */
    public ArrayList<Riddle> getAllRiddles(String Order) {
        if(matchesColumn(Order)) {
            throw new IllegalArgumentException("Ungültige Sortierreihenfolge: " + Order);
        }
        ArrayList<Riddle> All_Riddles = new ArrayList<Riddle>();
        Cursor cursor_Ar = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, null, null, null, null, Order);
        do {
            All_Riddles.add(cursorToRiddle(cursor_Ar, CursorToRiddleMode.MULTIPLE));
        } while(cursor_Ar.moveToNext());
        return All_Riddles;
    }

    /**
     * Liefert das Rätsel mit der angegebenen ID.
     * @param Id Integer : ID des Rätsels.
     * */
    public Riddle getRiddleById(int Id) {
        if(Id < 0) {
            throw new IllegalArgumentException("Ungültige Id: " + Id);
        }
        try {
            Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID + "=" + Id, null, null, null, null);
            return cursorToRiddle(cursor, CursorToRiddleMode.SINGLE);
        } catch( CursorIndexOutOfBoundsException ex) {
            Log.e(LOG_TAG, ex.getMessage());
            return null;
        }
    }


    /**
     * Liefert alle Rätsel mit einem bestimmten Rätselnamen.
     * @param Riddlename String : Name des Rätsels.
     * */
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


    /**
     * Liefert alle Rätsel mit einem bestimmten Rätselnamen
     * @param Riddlename String : Name des Rätsels.
     * @param Order String : Sortierreihenfolge der zurückgelieferten Liste.
     * */
    public ArrayList<Riddle> getRiddlesByName(String Riddlename, String Order) {
        if(!matchesColumn(Order)) {
            throw new IllegalArgumentException("Ungültige Sortiereihenfolge: " + Order);
        }
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


    /**
     * Liefert alle Rätsel eines Erstellers.
     * @param CreatorName String : Name des Erstellers.
     * */
    public ArrayList<Riddle> getRiddlesByCreator(String CreatorName) {
        ArrayList<Riddle> Riddles = new ArrayList<Riddle>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_CREATORNAME + "=" + CreatorName, null, null, null, null);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }


    /**
     * Liefert alle Rätsel eines Erstellers.
     * @param CreatorName String : Name des Erstellers.
     * @param Order String : Sortierung der zurückgelieferten Liste.
     * */
    public ArrayList<Riddle> getRiddlesByCreator(String CreatorName, String Order) {
        ArrayList<Riddle> Riddles = new ArrayList<Riddle>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_CREATORNAME + "=" + CreatorName, null, null, null, Order);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }


    /**
     * Liefert alle Rätsel mit dem entsprechenden Rating.
     * @param rating Float : Die gesuchte Bewertung.
     * */
    public ArrayList<Riddle> getRiddleByRatingExact(float rating) {
        if(rating > 5 || rating < 0) {
            throw new IllegalArgumentException("Das Rating muss zwischen 0 und 5 liegen.");
        }
        ArrayList<Riddle> Riddles = new ArrayList<>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_RATING + "=" + Float.toString(rating), null, null, null, null);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while (cursor.moveToNext());
        return Riddles;
    }

    /**
     * Liefert alle Rätsel mit einem Rating entsprechend des angegebenen Operators und Rating, sortiert nach der angegebenen Order.
     * @param rating Float : Der Wert der Bewertung nach der gesucht werden soll.
     * @param operator String : Der operator für das Selectstatement.
     * @param Order String : Die Sortierreihenfolge der zurückgelieferten Daten.
     * */
    public ArrayList<Riddle> getRiddleByRating(float rating, String operator, String Order) {
        if(rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Das Rating muss zwischen 0 und 5 liegen.");
        }
        if(!operator.matches("[<>=]{1}|(!=){1}")) {
            throw new IllegalArgumentException("Ungültiger Operator: " + operator);
        }
        if(!matchesColumn(Order)) {
            throw new IllegalArgumentException("Ungültige Reihenfolge: " + Order);
        }
        ArrayList<Riddle> Riddles = new ArrayList<>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, RiddleDbHelper.TABLE_RIDDLES_RATING + operator + Float.toString(rating), null, null, null, Order);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }

    /**
     * Liefert eine Liste von Rätseln nach dem Angegebenen Statement.
     * @param statement String : Ein Statement der Form COLUMNNAME_ID = ID.
     * */
    public ArrayList<Riddle> getRiddleBySQL(String statement) {
        ArrayList<Riddle> Riddles = new ArrayList<>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, statement, null, null, null, null);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }

    /**
     * Liefert eine Liste von Rätseln nach dem Angegebenen Statement.
     * @param statement String : Ein Statement der Form COLUMNNAME_ID = ID.
     * */
    public ArrayList<Riddle> getRiddleBySQL(String statement, String Order) {
        if(!matchesColumn(Order)) {
            throw new IllegalArgumentException("Ungültige Sortierreihenfolge");
        }
        ArrayList<Riddle> Riddles = new ArrayList<>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, statement, null, null, null, Order);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }


    /**
     * Liefert eine Liste von Rätseln abhänging von den Übergabeparameter.
     * @param statement String : Ein Statement der Form COLUMNNAME_ID = ID.
     * @param groupBy String : Spalte nach der Gruppiert werden soll.
     * @param having String : Having Argument des Statements.
     * @param Order String : Sortierreihenfolge der zurückgeliferten Liste.
     * */
    public ArrayList<Riddle> getRiddles(String statement, String groupBy, String having, String Order) {
        ArrayList<Riddle> Riddles = new ArrayList<>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, columns_all_riddles, statement, null, groupBy, having, Order);
        cursor.moveToFirst();
        do {
            Riddles.add(cursorToRiddle(cursor, CursorToRiddleMode.MULTIPLE));
        } while(cursor.moveToNext());
        return Riddles;
    }

    public ArrayList<String> getAllRiddleNames() {
        ArrayList<String> RiddleNames = new ArrayList<>();
        String[] Columns = {RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, Columns, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            try {
                RiddleNames.add(cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME)));
            } catch(Exception ex) {
                break;
            }
        }while(cursor.moveToNext());
        cursor.close();
        return RiddleNames;
    }

    public String getRiddleNameByID(int Id) {
        String[] Columns = {RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, Columns, Id + "=" + RiddleDbHelper.TABLE_RIDDLES_COLUMN_ID, null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME));
    }

    //Müsste theoretisch geändert werden von int auf ArrayList<int>
    public int getRiddleCheckpointCountByName(String RiddleName) {
        String[] Columns = {RiddleDbHelper.TABLE_RIDDLES_COUNT_QUESTIONS};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, Columns, RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME + "=" + "\""+ RiddleName + "\"", null, null, null, null);
        cursor.moveToFirst();
        return cursor.getInt(cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_COUNT_QUESTIONS));
    }

    public String getRiddleCreatorByName(String RiddleName) {
        String[] Columns = {RiddleDbHelper.TABLE_RIDDLES_CREATORNAME};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, Columns, RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME + "=" + "\"" + RiddleName + "\"", null, null, null, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_CREATORNAME));
    }

    public float getRiddleRatingByName(String RiddleName) {
        String[] Columns = {RiddleDbHelper.TABLE_RIDDLES_RATING};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_RIDDLES, Columns, RiddleDbHelper.TABLE_RIDDLES_RIDDLENAME + "=" + "\"" + RiddleName + "\"", null, null, null, null);
        cursor.moveToFirst();
        return cursor.getFloat(cursor.getColumnIndex(RiddleDbHelper.TABLE_RIDDLES_RATING));
    }

    //-----------------------------------------------------------------------USER-----------------------------------------------------------------------------------------------------


    /**
     * Fügt einen Nutzer in die Datenbank hinzu.
     * @param user User : Der Nutzer welcher der Datenbankhinzugefügt werden soll.
     * */
    public User setUserInDatabase(User user) {
        ContentValues values_user = new ContentValues();
        values_user.put(RiddleDbHelper.TABLE_USER_NAME, user.username);
        values_user.put(RiddleDbHelper.TABLE_USER_PASSWORD, user.password);
        long insertId = database.insert(RiddleDbHelper.TABLE_USER, null, values_user);

        Cursor cursor = database.query(RiddleDbHelper.TABLE_USER, columns_user, RiddleDbHelper.TABLE_USER_ID + "=" + insertId, null, null, null, null);
        cursor.moveToFirst();
        return cursorToUser(cursor);
    }

    /**
     * Erstellt aus einem Cursor auf einen Eintrag einen Nutzer.
     * @param cursor Cursor : Der Cursor auf den Eintrag in der Tabelle.
     * */
    public User cursorToUser(Cursor cursor) {
        String username = cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_USER_NAME));
        String password = cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_USER_PASSWORD));
        int id = cursor.getInt(cursor.getColumnIndex(RiddleDbHelper.TABLE_USER_ID));
        return new User(id, username, password);
    }


    /**
     * Liefert alle Nutzer in einer ArrayList<User>
     * */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = database.query(RiddleDbHelper.TABLE_USER, columns_user, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            users.add(cursorToUser(cursor));
        }while(cursor.moveToNext());
        return users;
    }

    /**
     * Liefert alle Usernames in einer ArrayList<String>
     * */
    public ArrayList<String> getAllUsernames() {
        ArrayList<String> usernames = new ArrayList<>();
        String[] columns = {RiddleDbHelper.TABLE_USER_NAME};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_USER, columns, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            usernames.add(cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_USER_NAME)));
        }while(cursor.moveToNext());
        return usernames;
    }


    /**
     * Liefert das Passwort zu einem User.
     * */
    public String getPasswordByUsername(String username) {
        String[] columns = {RiddleDbHelper.TABLE_USER_PASSWORD};
        Cursor cursor = database.query(RiddleDbHelper.TABLE_USER, columns, RiddleDbHelper.TABLE_USER_NAME + "=" + "\"" + username + "\"", null, null, null, null);
        if(cursor.getCount() < 1) {
            return null;
        }
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(RiddleDbHelper.TABLE_USER_PASSWORD));
    }
}
