package com.github.sebastiankg02.csy2061as2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.github.sebastiankg02.csy2061as2.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    private int id;
    private int userID;
    private DeliveryMethod delivery;
    private OrderStatus status;
    private LocalDateTime created;
    private ArrayList<OrderProduct> orderProducts;

    public Order(){
        this(-1, -1, 0, 0, "");
    }

    public Order(int id, int userID, int dMethod, int status, String created){
        this.id = id;
        this.userID = userID;
        this.delivery = DeliveryMethod.fromInt(dMethod);
        this.status = OrderStatus.fromInt(status);
        if(created.isEmpty()){
            this.created = LocalDateTime.now();
        } else {
            this.created = LocalDateTime.parse(created, User.formatter);
        }
        this.orderProducts = new ArrayList<OrderProduct>();
    }

    public Order updateProducts(Context c){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c, "OrderProduct", null, 1);
        this.orderProducts = opHelper.getAllOrderProductsInOrder(this.id);
        return this;
    }

    public ArrayList<OrderProduct> getProducts(Context c){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c, "OrderProduct", null, 1);
        return opHelper.getAllOrderProductsInOrder(this.id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public DeliveryMethod getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryMethod delivery) {
        this.delivery = delivery;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public ArrayList<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(ArrayList<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(""+
                    "CREATE TABLE m_order(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "USER_ID INTEGER NOT NULL," +
                    "DELIVERY INTEGER NOT NULL DEFAULT 0," +
                    "STATUS INTREGER NOT NULL DEFAULT 0," +
                    "CREATED TEXT NOT NULL," +
                    "FOREIGN KEY (USER_ID) REFERENCES user(ID))");
        }

        public ArrayList<Order> getAllOrders(){
            ArrayList<Order> output = new ArrayList<Order>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM m_order", null);
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new Order(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getInt(c.getColumnIndexOrThrow("USER_ID")),
                        c.getInt(c.getColumnIndexOrThrow("DELIVERY")),
                        c.getInt(c.getColumnIndexOrThrow("STATUS")),
                        c.getString(c.getColumnIndexOrThrow("CREATED"))
                ));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        public ArrayList<Order> getAllOrdersForUser(int userID){
            ArrayList<Order> output = new ArrayList<Order>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM m_order WHERE USER_ID = ?", new String[]{String.valueOf(userID)});
            c.moveToFirst();
            while(!c.isAfterLast()){
                output.add(new Order(
                        c.getInt(c.getColumnIndexOrThrow("ID")),
                        c.getInt(c.getColumnIndexOrThrow("USER_ID")),
                        c.getInt(c.getColumnIndexOrThrow("DELIVERY")),
                        c.getInt(c.getColumnIndexOrThrow("STATUS")),
                        c.getString(c.getColumnIndexOrThrow("CREATED"))
                ));
                c.moveToNext();
            }

            c.close();
            db.close();
            return output;
        }

        public boolean addOrder(Context c, Order o){
            SQLiteDatabase data = this.getWritableDatabase();
            ContentValues output = new ContentValues();

            output.put("USER_ID", o.userID);
            output.put("DELIVERY", o.delivery.value);
            output.put("STATUS", o.status.status);
            output.put("CREATED", User.formatter.format(LocalDateTime.now()));

            if(o.orderProducts != null){
                for(OrderProduct op: o.orderProducts){
                    OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c, "OrderProduct", null, 1);
                    opHelper.addOrderProduct(op);
                }
            }

            data.insert("m_order", null, output);
            data.close();
            return true;
        }

        public void addOrderFromBasket(Context c, HashMap<Integer, Integer> basket, User forUser, DeliveryMethod d){
            //Initialise new order object
            Order output = new Order();
            //Get next available Order ID - SQLite starts AUTOINCREMENT from 1, hence the +1
            int orderID = getAllOrders().size()+1;
            //Initialise DBHelper instances for Products and OrderProducts
            Product.DBHelper prodHelper = new Product.DBHelper(c, "Product", null, 1);
            OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c, "OrderProduct", null, 1);

            //Loop through products in basket
            for(int i: basket.keySet()){
                //Register OrderProduct against order, populate with data
                output.orderProducts.add(new OrderProduct(orderID, i, basket.get(i)));
                //Save created OrderProduct on database
                opHelper.addOrderProduct(output.getOrderProducts().get(output.orderProducts.size()-1));

                //Update product stock count on database
                prodHelper.updateProduct(prodHelper.getSpecificProduct(i).setStockLevel(prodHelper.getSpecificProduct(i).getStockLevel()-basket.get(i)));
            }
            output.userID = forUser.id;
            output.status = OrderStatus.CREATED;
            output.delivery = d;

            //Add order entry to database
            addOrder(c, output);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS m_order");
            onCreate(sqLiteDatabase);
        }
    }
}
