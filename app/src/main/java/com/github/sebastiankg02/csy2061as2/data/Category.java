package com.github.sebastiankg02.csy2061as2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Category {
    private int id;
    private String mainCategory;

    public Category(){
        this(-1, "");
    }

    public Category(String main){
        this(-1, main);
    }

    public Category(int id, String main){
        this.id = id;
        this.mainCategory = main;
    }

    public int getId() {
        return id;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "KWD", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase){
            try {
            sqLiteDatabase.execSQL("CREATE TABLE category(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "MAIN TEXT NOT NULL)");
            } catch (SQLException e){

            }
        }

        public void initDefaultCategories(){
            addCategory(new Category("Tech"));
            addCategory(new Category("Food"));
            addCategory(new Category("Household"));
            addCategory(new Category("Furniture"));
        }

        public boolean addCategory(Category c){
            for(Category o: getCategories()){
                if(o.mainCategory.equals(c.mainCategory)){
                    return false;
                }
            }

            SQLiteDatabase data = this.getWritableDatabase();
            ContentValues output = new ContentValues();

            output.put("MAIN", c.mainCategory);

            data.insert("category", null, output);
            data.close();
            return true;
        }

        public ArrayList<Category> getCategories(){
            ArrayList<Category> output = new ArrayList<Category>();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM category", null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new Category(c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getString(c.getColumnIndexOrThrow("MAIN"))));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        public Category getSpecificCategory(int id){
            Category output = new Category();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM category WHERE ID = ?", new String[]{String.valueOf(id)});
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.id = c.getInt(c.getColumnIndexOrThrow("ID"));
                output.mainCategory = c.getString(c.getColumnIndexOrThrow("MAIN"));
                c.moveToFirst();
            }

            c.close();
            db.close();
            return output;
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS category");
            onCreate(sqLiteDatabase);
        }

        @Override
        public void onOpen(SQLiteDatabase db){
            onCreate(db);
        }
    }
}
