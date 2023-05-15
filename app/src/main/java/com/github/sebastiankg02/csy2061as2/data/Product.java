package com.github.sebastiankg02.csy2061as2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.github.sebastiankg02.csy2061as2.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a product with various attributes such as id, name, description, category, price, list price,
 * retail price, created and updated date.
 */
public class Product {
    private int id;
    private String name;
    private String desc;
    private int category;
    private float price;
    private float listPrice;
    private float retailPrice;
    private LocalDateTime created;
    private LocalDateTime updated;
    private int stockLevel;

    /**
     * Constructs a new Product object with default values.
     * This constructor should only be used to create new Products, 
     * and its' fields should be overwritten before being saved in to the database.
     * 
     * The default values are:
     * -1 for id
     * "NO PRODUCT" for name
     * "PRODUCT DESCRIPTION" for desc
     * -1 for category
     * 0.0f for price
     * 0.0f for listPrice
     * 0.0f for retailPrice
     * LocalDateTime.now() for created and updated
     * 9999 for stockLevel
     */
    public Product(){
        this.id = -1;
        this.name = "NO PRODUCT";
        this.desc = "PRODUCT DESCRIPTION";
        this.category = -1;
        this.price = 0.0f;
        this.listPrice = 0.0f;
        this.retailPrice = 0.0f;
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
        this.stockLevel = 9999;
    }

    /**
     * Constructs a new Product object with the given parameters. This is a simplified version of the full constructor.
     *
     * @param pName The name of the product.
     * @param pDesc The description of the product.
     * @param pCat The category of the product.
     * @param price The price of the product.
     * @param lPrice The list price of the product.
     * @param rPrice The retail price of the product.
     * @param cTime The creation time of the product.
     * @param uTime The last update time of the product.
     * @param stock The stock level of the product.
     */
    public Product(String pName, String pDesc, int pCat, float price, float lPrice, float rPrice, LocalDateTime cTime, LocalDateTime uTime, int stock){
        this.id = -1;
        this.name = pName;
        this.desc = pDesc;
        this.category = pCat;
        this.price = price;
        this.listPrice = lPrice;
        this.retailPrice = rPrice;
        this.created = cTime;
        this.updated = uTime;
        this.stockLevel = stock;
    }

    /**
     * Constructs a new Product object with the given parameters.
     * This constructor is used to initalise loaded data from the database.
     *
     * @param id The unique identifier for the product.
     * @param pName The name of the product.
     * @param pDesc The description of the product.
     * @param pCat The category of the product.
     * @param price The price of the product.
     * @param lPrice The list price of the product.
     * @param rPrice The retail price of the product.
     * @param cTime The creation time of the product.
     * @param uTime The last update time of the product.
     * @param stock The stock level of the product.
     */
    public Product(int id, String pName, String pDesc, int pCat, float price, float lPrice, float rPrice, LocalDateTime cTime, LocalDateTime uTime, int stock){
        this.id = id;
        this.name = pName;
        this.desc = pDesc;
        this.category = pCat;
        this.price = price;
        this.listPrice = lPrice;
        this.retailPrice = rPrice;
        this.created = cTime;
        this.updated = uTime;
        this.stockLevel = stock;
    }

    /**
     * Returns the current stock level of the product.
     *
     * @return The current stock level of the product.
     */
    public int getStockLevel() {
        return stockLevel;
    }

    /**
     * Sets the stock level of the product.
     *
     * @param stockLevel The new stock level of the product.
     * @return The updated product object.
     */
    public Product setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
        return this;
    }

    /**
     * Sets the ID of this product
     *
     * @param id The new ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the ID of this product
     *
     * @return The ID of this product
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the list price of the product
     *
     * @return The list price of the product
     */
    public float getListPrice() {
        return listPrice;
    }

    /**
     * Returns the price of the product.
     *
     * @return The price of the product.
     */
    public float getPrice() {
        return price;
    }

    /**
     * Returns the retail price of the product.
     *
     * @return The retail price of the product.
     */
    public float getRetailPrice() {
        return retailPrice;
    }

    /**
     * Returns the category ID of this product
     *
     * @return The category ID of this product
     */
    public int getCategory() {
        return category;
    }

    /**
     * Returns the LocalDateTime when this product was created.
     *
     * @return The LocalDateTime when this product was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Returns the LocalDateTime when this product was last updated.
     *
     * @return The LocalDateTime when this product was last updated.
     */
    public LocalDateTime getUpdated() {
        return updated;
    }

    /**
     * Returns the description of this product
     *
     * @return The description of this product
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Returns the name of this product
     *
     * @return The name of this product
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the category ID of this product 
     *
     * @param category The new category ID to set for this product
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Sets the creation LocalDateTime of this product
     *
     * @param created The new LocalDateTime the product was created.
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Sets the description of this product
     *
     * @param desc The new description to set.
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Sets the list price of a product.
     *
     * @param listPrice The list price of the product.
     */
    public void setListPrice(float listPrice) {
        this.listPrice = listPrice;
    }

    /**
     * Sets the name of this product.
     *
     * @param name The new name to set for this product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the price of the product.
     *
     * @param price The price of the product.
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * Sets the retail price of the product.
     *
     * @param retailPrice The retail price of the product.
     */
    public void setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * Sets the updated time of this product to the specified LocalDateTime.
     *
     * @param updated The updated time to set for this product
     */
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    /**
     * A helper class for managing the SQLite database for the app.
     * It provides methods for creating, reading, updating, and deleting products from the database.
     */
    public static class DBHelper extends SQLiteOpenHelper {

        /**
         * Constructs a new DBHelper object with the given context.
         *
         * @param context The context to use for the DBHelper object.
         */
        public DBHelper(Context context) {
            super(context, "KWD", null, 1);
        }

        /**
         * Called when the database is created for the first time. This method creates the "product" table
         * with the specified columns and constraints.
         *
         * @param sqLiteDatabase The database to create the table in.
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
            sqLiteDatabase.execSQL(""+
                    "CREATE TABLE product(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT NOT NULL," +
                    "DESCRIPTION TEXT NOT NULL," +
                    "CATEGORY INTEGER NOT NULL," +
                    "PRICE REAL NOT NULL," +
                    "LIST_PRICE REAL NOT NULL," +
                    "RETAIL_PRICE REAL NOT NULL," +
                    "CREATED TEXT NOT NULL," +
                    "UPDATED TEXT," +
                    "STOCK INTEGER NOT NULL DEFAULT 0," +
                    "FOREIGN KEY (CATEGORY) REFERENCES category(ID))");
            } catch (SQLException e){

            }
        }

        /**
         * Initializes the default products for the store.
         * Adds a set of pre-defined products to the store inventory.
         */
        public void initDefaultProducts(){
            addProduct(new Product(
                    "16GB DDR4 RAM (3600MHz, CL27)",
                    "16GB of DDR4 RAM, Factory overclocked to run at 3600MHz, CL27 speed. New.",
                    1,
                    65.0f,
                    75.0f,
                    50.0f,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    354));
            addProduct(new Product(
                    "8GB DDR4 RAM (3600MHz, CL25)",
                    "8GB of DDR4 RAM, Factory overclocked to run at 3600MHz, CL25 speed. New.",
                    1,
                    40.0f,
                    45.0f,
                    35.0f,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    523));
            addProduct(new Product(
                    "Apples (Granny Smith, individual)",
                    "Granny Smith Apples. Nice and red. Fresh & fruity.",
                    2,
                    0.35f,
                    0.5f,
                    0.1f,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    23000));
            addProduct(new Product(
                    "Apples (Generic, individual)",
                    "Generic apples. Still fresh & still fruity!",
                    2,
                    0.15f,
                    0.25f,
                    0.02f,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    3021));
            addProduct(new Product(
                    "Bananas (individual)",
                    "The classic fruit - perfectly ripe & yellow!",
                    2,
                    0.18f,
                    0.29f,
                    0.09f,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    728));
            addProduct(new Product(
                    "Sugar (1kg)",
                    "Sweet! Now in brand-new 1kg size!",
                    2,
                    1.75f,
                    2.25f,
                    1.12f,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    45));
        }

        /**
         * Adds a new product to the database if it does not already exist.
         *
         * @param p The product to add to the database.
         * @return true if the product was added successfully, false if the product already exists in the database.
         */
        public boolean addProduct(Product p){
            for(Product o: getAllProducts()){
                if(o.name.equals(p.name)){
                    return false;
                }
            }

            SQLiteDatabase data = this.getWritableDatabase();
            ContentValues output = new ContentValues();

            output.put("NAME", p.name);
            output.put("DESCRIPTION", p.desc);
            output.put("CATEGORY", p.category);
            output.put("PRICE", p.price);
            output.put("LIST_PRICE", p.listPrice);
            output.put("RETAIL_PRICE", p.retailPrice);
            output.put("CREATED", User.formatter.format(p.created));
            output.put("UPDATED", User.formatter.format(p.updated));
            output.put("STOCK", p.stockLevel);

            data.insert("product", null, output);
            data.close();
            return true;
        }

        /**
         * Retrieves all products from the database and returns them as an ArrayList.
         *
         * @return An ArrayList of all products in the database.
         */
        public ArrayList<Product> getAllProducts(){
            ArrayList<Product> output = new ArrayList<Product>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM product", null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new Product(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getString(c.getColumnIndexOrThrow("NAME")),
                        c.getString(c.getColumnIndexOrThrow("DESCRIPTION")),
                        c.getInt(c.getColumnIndexOrThrow("CATEGORY")),
                        c.getFloat(c.getColumnIndexOrThrow("PRICE")),
                        c.getFloat(c.getColumnIndexOrThrow("LIST_PRICE")),
                        c.getFloat(c.getColumnIndexOrThrow("RETAIL_PRICE")),
                        LocalDateTime.parse(c.getString(c.getColumnIndexOrThrow("CREATED")), User.formatter),
                        LocalDateTime.parse(c.getString(c.getColumnIndexOrThrow("UPDATED")), User.formatter),
                        c.getInt(c.getColumnIndexOrThrow("STOCK"))));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        /**
         * Retrieves all products in a given category from the database.
         *
         * @param cID The ID of the category to retrieve products from.
         * @return An ArrayList of Product objects in the given category.
         */
        public ArrayList<Product> getAllProductsInCategory(int cID){
            ArrayList<Product> output = new ArrayList<Product>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM product WHERE CATEGORY = ?", new String[]{String.valueOf(cID)});
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new Product(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getString(c.getColumnIndexOrThrow("NAME")),
                        c.getString(c.getColumnIndexOrThrow("DESCRIPTION")),
                        c.getInt(c.getColumnIndexOrThrow("CATEGORY")),
                        c.getFloat(c.getColumnIndexOrThrow("PRICE")),
                        c.getFloat(c.getColumnIndexOrThrow("LIST_PRICE")),
                        c.getFloat(c.getColumnIndexOrThrow("RETAIL_PRICE")),
                        LocalDateTime.parse(c.getString(c.getColumnIndexOrThrow("CREATED")), User.formatter),
                        LocalDateTime.parse(c.getString(c.getColumnIndexOrThrow("UPDATED")), User.formatter),
                        c.getInt(c.getColumnIndexOrThrow("STOCK"))));
                c.moveToNext();
            }

            c.close();
            db.close();

            return output;
        }

        /**
         * Updates a product in the database with the given new product information.
         *
         * @param newProduct The new product information to update the database with.
         */
        public void updateProduct(Product newProduct){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("NAME", newProduct.name);
            cv.put("DESCRIPTION", newProduct.desc);
            cv.put("CATEGORY", newProduct.category);
            cv.put("PRICE", newProduct.price);
            cv.put("LIST_PRICE", newProduct.listPrice);
            cv.put("RETAIL_PRICE", newProduct.retailPrice);
            cv.put("CREATED", User.formatter.format(newProduct.created));
            cv.put("UPDATED", User.formatter.format(LocalDateTime.now()));
            cv.put("STOCK", newProduct.stockLevel);

            db.update("product", cv, "ID = ?", new String[]{String.valueOf(newProduct.id)});
            db.close();
        }

        /**
         * Deletes a product from the database.
         *
         * @param toDelete The product to be deleted.
         */
        public void deleteProduct(Product toDelete){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("product", "ID = ?", new String[]{String.valueOf(toDelete.getId())});
            db.close();
        }

        /**
         * Returns a specific product by its ID.
         *
         * @param pID The ID of the product to retrieve.
         * @return The product with the specified ID, or null if it does not exist.
         */
        public Product getSpecificProduct(int pID){
            for(Product p: getAllProducts()){
                if(p.id  == pID){
                    return p;
                }
            }
            return null;
        }

        /**
         * Called when the database needs to be upgraded. Drops the existing "product" table and creates a new one.
         *
         * @param sqLiteDatabase The database to upgrade
         * @param i The old version of the database
         * @param i1 The new version of the database
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS product");
            onCreate(sqLiteDatabase);
        }

        /**
         * Called when the database has been opened. This method is called after the database connection has been established
         * and before any other methods are called on the database instance.
         *
         * @param db The database that has been opened.
         */
        @Override
        public void onOpen(SQLiteDatabase db){
            onCreate(db);
        }
    }
}
