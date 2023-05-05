package com.github.sebastiankg02.csy2061as2.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class User {
    public static final char[] passwordChars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    public static final char[] passwordNumbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static final int passwordMinChars = 8;

    public int id;
    public String fullName;
    public String email;
    public LocalDateTime registeredDate;
    public LocalDateTime updatedDate;
    public String password;
    public String hobbies;
    public String address;
    public UserAccessLevel level;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public User(){
        this.id = 0;
        this.fullName = "NO NAME";
        this.email = "no@email.found";
        this.registeredDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.password = "password";
        this.hobbies = "";
        this.address = "SOMEWHERE AND NOWHERE";
        this.level = UserAccessLevel.USER;
    }

    public User(int id, String name, String email, String registered, String updated, String password, String hobbies, String address, int level){
        this.id = id;
        this.fullName = name;
        this.email = email;
        this.registeredDate = LocalDateTime.parse(registered, formatter);
        this.updatedDate = LocalDateTime.parse(updated, formatter);
        this.password = password;
        this.hobbies = hobbies;
        this.address = address;
        this.level = UserAccessLevel.getLevelFromInt(level);
    }

    public User(int id, String name, String email, LocalDateTime registered, LocalDateTime updated, String password, String address){
        this.id = id;
        this.fullName = name;
        this.email = email;
        this.registeredDate = registered;
        this.updatedDate = updated;
        this.password = password;
        this.hobbies = "";
        this.address = address;
        this.level = UserAccessLevel.getLevelFromInt(0);
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("" +
                    "CREATE TABLE user(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT NOT NULL," +
                    "EMAIL TEXT NOT NULL," +
                    "REGISTERED TEXT NOT NULL," +
                    "UPDATED TEXT," +
                    "PASSWORD TEXT NOT NULL," +
                    "HOBBIES TEXT," +
                    "ADDRESS TEXT NOT NULL," +
                    "LEVEL INT NOT NULL)");
        }

        public boolean addUser(User u){
            for(User o: getUsers()){
                if(o.email.equals(u.email)){
                    return false;
                }
            }

            SQLiteDatabase data = this.getWritableDatabase();
            ContentValues output = new ContentValues();

            output.put("NAME", u.fullName);
            output.put("EMAIL", u.email);
            output.put("REGISTERED", formatter.format(u.registeredDate));

            if (u.updatedDate != null) {
                output.put("UPDATED", formatter.format(u.registeredDate));
            }

            output.put("PASSWORD", u.password);

            if (u.hobbies != null) {
                output.put("HOBBIES", u.hobbies);
            }

            output.put("ADDRESS", u.address);
            output.put("LEVEL", u.level.value);

            data.insert("user", null, output);
            data.close();
            return true;
        }

        public void updateUser(User newUser, String username){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("NAME", newUser.fullName);
            cv.put("UPDATED", formatter.format(LocalDateTime.now()));
            cv.put("PASSWORD", newUser.password);
            cv.put("HOBBIES", newUser.hobbies);
            cv.put("ADDRESS", newUser.address);
            db.update("user", cv, "EMAIL = ?", new String[]{username});
            db.close();
        }

        public ArrayList<User> getUsers(){
            ArrayList<User> output = new ArrayList<User>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM user", null);
            c.moveToFirst();
            while(c.isAfterLast() == false){
                int id = c.getInt(c.getColumnIndexOrThrow("ID"));
                String name = c.getString(c.getColumnIndexOrThrow("NAME"));
                String email = c.getString(c.getColumnIndexOrThrow("EMAIL"));
                String registered = c.getString(c.getColumnIndexOrThrow("REGISTERED"));
                String updated = c.getString(c.getColumnIndexOrThrow("UPDATED"));
                String password = c.getString(c.getColumnIndexOrThrow("PASSWORD"));
                String hobbies = c.getString(c.getColumnIndexOrThrow("HOBBIES"));
                String address = c.getString(c.getColumnIndexOrThrow("ADDRESS"));
                int userLevel = c.getInt(c.getColumnIndexOrThrow("LEVEL"));

                User u = new User(id, name, email, registered, updated, password, hobbies, address, userLevel);
                output.add(u);
                c.moveToNext();
            }

            db.close();
            return output;
        }

        public User getSpecificUser(String email, String password){
            for(User u: getUsers()){
                if(u.email.equals(email) && u.password.equals(password)){
                    return u;
                }
            }
            return null;
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
        }
    }
}
