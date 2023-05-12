package com.github.sebastiankg02.csy2061as2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Represents a Product Category with an ID (used internally in SQLite DB) and a main category name.
 */
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

    /**
     * A helper class for managing the SQLite database for the app.
     */
    public static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "KWD", null, 1);
        }

        /**
         * Called when the database is created for the first time. This is where the creation of
         * tables and the initial population of the tables should happen.
         *
         * @param sqLiteDatabase The database.
         * @throws SQLException if there is an error creating the table.
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase){
            try {
            sqLiteDatabase.execSQL("CREATE TABLE category(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "MAIN TEXT NOT NULL)");
            } catch (SQLException e){

            }
        }

        /**
         * Initializes the default categories for the application.
         * Categories include Tech, Food, Household, and Furniture.
         */
        public void initDefaultCategories(){
            addCategory(new Category("Tech"));
            addCategory(new Category("Food"));
            addCategory(new Category("Household"));
            addCategory(new Category("Furniture"));
        }

        /**
         * Adds a new category to the database if it does not already exist.
         *
         * @param c The category to add to the database.
         * @return True if the category was added successfully, false if it already exists in the database.
         */
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

        /**
         * Updates the category in the database with the given category object.
         *
         * @param c The category object to update in the database.
         */
        public void updateCategory(Category c){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("MAIN", c.mainCategory);
            db.update("category", cv, "ID = ?", new String[]{String.valueOf(c.id)});
            db.close();
        }

        /**
         * Retrieves all categories from the database and returns them as an ArrayList.
         *
         * @return An ArrayList of Category objects representing all categories in the database.
         */
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

        /**
         * Retrieves a specific category from the database by its ID.
         *
         * @param id The ID of the category to retrieve.
         * @return The Category object with the specified ID, or an empty Category object if not found.
         */
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

        /**
         * Deletes a category from the database.
         *
         * @param toDelete The category to be deleted.
         */
        public void deleteCategory(Category toDelete){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("category", "ID = ?", new String[]{String.valueOf(toDelete.getId())});
            db.close();
        }

        /**
         * Called when the database needs to be upgraded. This method drops the existing "category" table
         * and creates a new one.
         *
         * @param sqLiteDatabase The database to be upgraded
         * @param i The old version of the database
         * @param i1 The new version of the database
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS category");
            onCreate(sqLiteDatabase);
        }

        /**
         * Called when a database is opened. This method is called after the database connection has been
         * configured and opened successfully.
         *
         * @param db The database that is being opened.
         */
        @Override
        public void onOpen(SQLiteDatabase db){
            onCreate(db);
        }
    }
}
