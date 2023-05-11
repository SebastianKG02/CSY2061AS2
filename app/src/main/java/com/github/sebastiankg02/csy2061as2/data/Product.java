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

    public int getStockLevel() {
        return stockLevel;
    }

    public Product setStockLevel(int stockLevel) {
        this.stockLevel = stockLevel;
        return this;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getListPrice() {
        return listPrice;
    }

    public float getPrice() {
        return price;
    }

    public float getRetailPrice() {
        return retailPrice;
    }

    public int getCategory() {
        return category;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setListPrice(float listPrice) {
        this.listPrice = listPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setRetailPrice(float retailPrice) {
        this.retailPrice = retailPrice;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "KWD", null, 1);
        }

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
            cv.put("UPDATED", User.formatter.format(newProduct.updated));
            cv.put("STOCK", newProduct.stockLevel);

            db.update("product", cv, "ID = ?", new String[]{String.valueOf(newProduct.id)});
            db.close();
        }

        public Product getSpecificProduct(int pID){
            for(Product p: getAllProducts()){
                if(p.id  == pID){
                    return p;
                }
            }
            return new Product();
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS product");
            onCreate(sqLiteDatabase);
        }

        @Override
        public void onOpen(SQLiteDatabase db){
            onCreate(db);
        }
    }
}
