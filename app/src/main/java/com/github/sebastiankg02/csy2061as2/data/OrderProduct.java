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
 * Represents a product in an order, with its ID, order ID, product ID, and quantity.
 */
public class OrderProduct {
    private int id;
    private int order;
    private int product;
    private int quantity;

    /**
     * Constructs an OrderProduct object with default values.
     * The default values are:
     * -1 for the ID, order ID, product ID
     * 0 for the quantity of the product in the order.
     * 
     * The user should NEVER see this type of OrderProduct!
     */
    public OrderProduct(){
        this(-1, -1, -1, 0);
    }

    /**
     * Constructs an OrderProduct object with the given order ID, product ID, and quantity.
     * This constructor is used when the OrderProduct object is not yet assigned an ID,
     * as the ID itself will be automatically assigned by the database.
     *
     * @param order The ID of the order associated with this OrderProduct
     * @param product The ID of the product associated with this OrderProduct
     * @param quantity The quantity of the product associated with this OrderProduct
     */
    public OrderProduct(int order, int product, int quantity){
        this(-1, order, product, quantity);
    }

    /**
     * Constructs an OrderProduct object with the given parameters.
     * This is used to construct OrderProducts fetched from the database.
     * 
     * @param id The ID of the order product.
     * @param order The ID of the order this product belongs to.
     * @param product The ID of the product this OrderProduct references.
     * @param quantity The quantity of the product in the order.
     */
    public OrderProduct(int id, int order, int product, int quantity){
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Returns the ID of this OrderProduct
     *
     * @return The ID of this OrderProduct
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of this OrderProduct
     *
     * @param id The new ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the order ID of this OrderProduct
     *
     * @return The order ID of this OrderProduct
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the order ID of this OrderProduct
     *
     * @param order The new order ID of this OrderProduct
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Returns the product ID of this OrderProduct
     *
     * @return The product ID of this OrderProduct
     */
    public int getProduct() {
        return product;
    }

    /**
     * Sets the product ID of this OrderProduct
     *
     * @param product The product ID to set.
     */
    public void setProduct(int product) {
        this.product = product;
    }

    /**
     * Returns the quantity of products in this OrderProduct
     *
     * @return The quantity of products in this OrderProduct
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of products in this OrderProduct
     *
     * @param quantity The new quantity of products in this OrderProduct
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Deletes the current OrderProduct from the database.
     *
     * @param c The context of the application.
     */
    public void delete(Context c){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c);
        opHelper.removeOrderProduct(this);
    }

    /**
     * A helper class for managing the SQLite database for the app.
     * This class extends the SQLiteOpenHelper class and provides methods for
     * creating, reading, updating, and deleting order products from the database.
     */
    public static class DBHelper extends SQLiteOpenHelper {

        /**
         * Constructs a new DBHelper object with the given context.
         *
         * @param c The context to use for the DBHelper object.
         */
        public DBHelper(Context c){
            super(c, "KWD", null, 1);
        }

        /**
         * Called when the database is created. Creates a table for order products with columns for ID, order ID, product ID,
         * and quantity. Also sets up foreign key constraints for the order ID and product ID columns.
         *
         * @param sqLiteDatabase The database to create the table in.
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL("" +
                        "CREATE TABLE order_product(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "ORDER_ID INTEGER NOT NULL," +
                        "PRODUCT_ID INTEGER NOT NULL," +
                        "QUANTITY INTEGER NOT NULL DEFAULT 0," +
                        "FOREIGN KEY (ORDER_ID) REFERENCES m_order(ID)," +
                        "FOREIGN KEY (PRODUCT_ID) REFERENCES product(ID))");
            } catch (SQLException e){

            }
        }

        /**
         * Retrieves all order products from the database.
         *
         * @return An ArrayList of OrderProduct objects representing all order products in the database.
         */
        public ArrayList<OrderProduct> getAllOrderProducts(){
            ArrayList<OrderProduct> output = new ArrayList<OrderProduct>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM order_product", null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new OrderProduct(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getInt(c.getColumnIndexOrThrow("ORDER_ID")),
                        c.getInt(c.getColumnIndexOrThrow("PRODUCT_ID")),
                        c.getInt(c.getColumnIndexOrThrow("QUANTITY"))
                ));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        /**
         * Retrieves all the order products associated with a given order ID from the database.
         *
         * @param orderID The ID of the order to retrieve the products for.
         * @return An ArrayList of OrderProduct objects associated with the given order ID.
         */
        public ArrayList<OrderProduct> getAllOrderProductsInOrder(int orderID){
            ArrayList<OrderProduct> output = new ArrayList<OrderProduct>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM order_product WHERE ORDER_ID = ?", new String[]{String.valueOf(orderID)});
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new OrderProduct(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getInt(c.getColumnIndexOrThrow("ORDER_ID")),
                        c.getInt(c.getColumnIndexOrThrow("PRODUCT_ID")),
                        c.getInt(c.getColumnIndexOrThrow("QUANTITY"))
                ));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        /**
         * Retrieves all order products that match the given product ID.
         *
         * @param productID The ID of the product to match.
         * @return An ArrayList of OrderProduct objects that match the given product ID.
         */
        public ArrayList<OrderProduct> getAllOrderProductsByProduct(int productID){
            ArrayList<OrderProduct> output = new ArrayList<OrderProduct>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM order_product WHERE PRODUCT_ID = ?", new String[]{String.valueOf(productID)});
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new OrderProduct(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getInt(c.getColumnIndexOrThrow("ORDER_ID")),
                        c.getInt(c.getColumnIndexOrThrow("PRODUCT_ID")),
                        c.getInt(c.getColumnIndexOrThrow("QUANTITY"))
                ));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        /**
         * Adds an order product to the database if it does not already exist.
         *
         * @param op The order product to add to the database.
         * @return True if the order product was added successfully, false if it already exists in the database.
         */
        public boolean addOrderProduct(OrderProduct op){
            for(OrderProduct o: getAllOrderProducts()){
                if(o.order == op.order && o.product == op.product && o.quantity == op.quantity){
                    return false;
                }
            }

            SQLiteDatabase data = this.getWritableDatabase();
            ContentValues output = new ContentValues();

            output.put("ORDER_ID", op.order);
            output.put("PRODUCT_ID", op.product);
            output.put("QUANTITY", op.quantity);

            data.insert("order_product", null, output);
            data.close();

            return true;
        }

        /**
         * Removes an order product from the database.
         *
         * @param toRemove The order product to remove.
         */
        public void removeOrderProduct(OrderProduct toRemove){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("order_product", "ID = ?", new String[]{String.valueOf(toRemove.id)});
            db.close();
        }

        /**
         * Called when the database needs to be upgraded. This method drops the existing table
         * "order_product" and creates a new one.
         *
         * @param sqLiteDatabase The database to be upgraded
         * @param i The old version of the database
         * @param i1 The new version of the database
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS order_product");
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
