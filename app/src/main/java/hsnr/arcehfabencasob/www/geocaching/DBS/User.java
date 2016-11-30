package hsnr.arcehfabencasob.www.geocaching.DBS;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Carsten on 28.11.2016.
 */

public class User {

    public String username;
    public String password;

    public User(String username, String password) {
        if(username == null || username.equals("") || password == null || password.equals("")) {
            throw new IllegalArgumentException("Username oder Passwort leer.");
        }
        try {
            this.username = username;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hash = new String(messageDigest.digest());
            this.password = hash;
        } catch(NoSuchAlgorithmException ex) {
            return;
        }

    }

    static public boolean compareLoginCredentials(String username, String password, RiddleDataSource database) {

        String passwd = database.getPasswordByUsername(username);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            String hash = new String(messageDigest.digest());
            if(hash.equals(passwd)) {
                return true;
            }
        } catch(NoSuchAlgorithmException ex) {
            Log.e("FEHLER IN USER - ", ex.getMessage());
            return false;
        }
        return false;
    }

}
