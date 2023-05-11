package com.github.sebastiankg02.csy2061as2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OrderProduct {
    private int id;
    private int order;
    private int product;
    private int quantity;

    public OrderProduct(){
        this(-1, -1, -1, 0);
    }

    public OrderProduct(int order, int product, int quantity){
        this(-1, order, product, quantity);
    }

    public OrderProduct(int id, int order, int product, int quantity){
        this.id = id;
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void delete(Context c){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c);
        opHelper.removeOrderProduct(this);
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context c){
            super(c, "KWD", null, 1);
        }

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

        public void removeOrderProduct(OrderProduct toRemove){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete("order_product", "ID = ?", new String[]{String.valueOf(toRemove.id)});
            db.close();
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS order_product");
            onCreate(sqLiteDatabase);
        }

        @Override
        public void onOpen(SQLiteDatabase db){
            onCreate(db);
        }
    }
}
