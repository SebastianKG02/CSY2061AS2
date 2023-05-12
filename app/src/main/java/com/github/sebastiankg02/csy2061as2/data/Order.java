package com.github.sebastiankg02.csy2061as2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.sebastiankg02.csy2061as2.user.User;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents an order made by a user, containing information such as the order ID, user ID,
 * delivery method, order status, creation date, and a list of products in the order.
 */
public class Order {
    private int id;
    private int userID;
    private DeliveryMethod delivery;
    private OrderStatus status;
    private LocalDateTime created;
    private ArrayList<OrderProduct> orderProducts;

    /**
     * Constructs an Order object with default values.
     * The default values are:
     * -1 for both customer ID and product ID
     * 0 for both delivery method and status (which equate to NONE in their respective enums)
     * An empty string for the order creation date
     */
    public Order(){
        this(-1, -1, 0, 0, "");
    }

    /**
     * Constructs an Order object with the given parameters. 
     * This constructor is used to initalise Order objects loaded in from the database.
     *
     * @param id The ID of the order
     * @param userID The ID of the user who placed the order
     * @param dMethod The delivery method for the order
     * @param status The status of the order
     * @param created The date and time the order was created
     */
    public Order(int id, int userID, int dMethod, int status, String created){
        this.id = id;
        this.userID = userID;
        this.delivery = DeliveryMethod.fromInt(dMethod);
        this.status = OrderStatus.fromInt(status);
        /**
         * Sets the creation time of the order. If the created string is empty, the current time is used.
         * Otherwise, the created string is parsed into a LocalDateTime object using the formatter.
         */
        if(created.isEmpty()){
            this.created = LocalDateTime.now();
        } else {
            this.created = LocalDateTime.parse(created, User.formatter);
        }
        this.orderProducts = new ArrayList<OrderProduct>();
    }

    /**
     * Updates the list of products registered against the order (OrderProduct object instances) by retrieving them from the database.
     *
     * @param c The context of the application.
     * @return The updated order object.
     */
    public Order updateProducts(Context c){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c);
        this.orderProducts = opHelper.getAllOrderProductsInOrder(this.id);
        return this;
    }

    /**
     * Returns an ArrayList of all the products registered against the order (OrderProducts)
     *
     * @param c The context of the application.
     * @return An ArrayList of OrderProduct objects.
     */
    public ArrayList<OrderProduct> getProducts(Context c){
        OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c);
        return opHelper.getAllOrderProductsInOrder(this.id);
    }

    /**
     * Returns the ID of this Order
     *
     * @return The ID of this Order
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of this Order
     *
     * @param id The new ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the user ID that created this order
     *
     * @return The user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID that created this order
     *
     * @param userID The user ID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Returns the delivery method of this object.
     *
     * @return The delivery method of this object.
     */
    public DeliveryMethod getDelivery() {
        return delivery;
    }

    /**
     * Sets the delivery method for this object.
     *
     * @param delivery The delivery method to set.
     */
    public void setDelivery(DeliveryMethod delivery) {
        this.delivery = delivery;
    }

    /**
     * Returns the current status of the order.
     *
     * @return The current status of the order.
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the order.
     *
     * @param status The new status of the order.
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    /**
     * Returns the LocalDateTime when this order was created.
     *
     * @return The LocalDateTime when this order was created.
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * Sets the creation LocalDateTime of the order
     *
     * @param created The LocalDateTime to set
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * Returns the list of products registered against this order.
     *
     * @return The list of products registered against this order.
     */
    public ArrayList<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    /**
     * Requests an update to the status of an order based on its current status and the time elapsed since its creation.
     * The status of the order is updated based on the time elapsed and the speed factor of the delivery.
     * If the status is updated, the order is updated in the database.
     * If the status is updated, the method is called recursively to check if the status needs to be updated again.
     *
     * @param c The context of the application.
     */
    public void requestOrderStatusUpdate(Context c){
        int speedFactor = delivery.maximumDays;
        LocalDateTime createdTime = created;
        boolean hasUpdatedStatus = false;

        /*
         * Updates the status of an order based on its current status and the time elapsed since its creation.
         */
        switch(status){
            case CREATED:
                if(LocalDateTime.now().isAfter(created.plusMinutes(30L * speedFactor))){
                    status = OrderStatus.PACKING;
                    hasUpdatedStatus = true;
                }
                break;
            case PACKING:
                if(LocalDateTime.now().isAfter(createdTime.plusHours(2L * speedFactor))){
                    status = OrderStatus.PRE_DISPATCH;
                    hasUpdatedStatus = true;
                }
                break;
            case PRE_DISPATCH:
                if(LocalDateTime.now().isAfter(createdTime.plusHours(8L * speedFactor))){
                    status = OrderStatus.DISPATCHED;
                    hasUpdatedStatus = true;
                }
                break;
            case DISPATCHED:
                if(LocalDateTime.now().isAfter(createdTime.plusHours(12L * speedFactor))){
                    status = OrderStatus.DELIVERED;
                    hasUpdatedStatus = true;
                }
                break;
            case START_RETURN:
                if(LocalDateTime.now().isAfter(createdTime.plusHours(12L))){
                    status = OrderStatus.RETURNED;
                    updateOrder(c, true);
                }
                break;
        }

        Order.DBHelper orderHelper = new Order.DBHelper(c);
        orderHelper.updateOrder(this);

        if(hasUpdatedStatus){
            requestOrderStatusUpdate(c);
        }
    }

    /**
     * Calculates the total number of products in the order.
     *
     * @return The total number of products in the order.
     */
    public int getOrderProductCount(){
        int output = 0;
        for(OrderProduct op: orderProducts){
            output += op.getQuantity();
        }

        return output;
    }

    /**
     * Calculates the total price of an order, including delivery cost and VAT,
     * based on the OrderProducts within the order.
     *
     * @param c The context of the application.
     * @return A formatted string representing the total price, including VAT.
     */
    public String calculateTotalPrice(Context c){
        Product.DBHelper productHelper = new Product.DBHelper(c);
        updateProducts(c);

        float price = delivery.cost;
        for(OrderProduct op: orderProducts){
            price += productHelper.getSpecificProduct(op.getProduct()).getPrice() * op.getQuantity();
        }
        return "£" + new DecimalFormat("#.0#").format(price) + "\nincluding VAT of £" + new DecimalFormat("#.0#").format(price - (price / 1.2f));
    }

    /**
     * Updates the order in the database and, if specified, cancels the order by updating the stock levels of the products
     * and deleting the order products from the database.
     *
     * @param c The context of the application
     * @param cancel Whether or not to cancel the order
     */
    public void updateOrder(Context c, boolean cancel){
        Order.DBHelper orderHelper = new Order.DBHelper(c);
        orderHelper.updateOrder(this);

        if(cancel) {
            Product.DBHelper productHelper = new Product.DBHelper(c);
            Product temp = new Product();
            for (OrderProduct op : orderProducts) {
                temp = productHelper.getSpecificProduct(op.getProduct());
                temp.setStockLevel(temp.getStockLevel() + op.getQuantity());
                productHelper.updateProduct(temp);
                op.delete(c);
            }
        }
    }

    /**
     * Deletes the current order from the database.
     *
     * @param c The context of the application.
     */
    public void delete(Context c){
        Order.DBHelper orderHelper = new Order.DBHelper(c);
        orderHelper.removeOrder(this, c);
    }

    /**
     * Sets the list of OrderProducts for this Order.
     *
     * @param orderProducts The list of OrderProducts to set.
     */
    public void setOrderProducts(ArrayList<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    /**
     * A helper class for managing the SQLite database table for orders.
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
         * Called when the database is created for the first time. This method creates a new table
         * called "m_order" with columns for ID, USER_ID, DELIVERY, STATUS, and CREATED. The ID column
         * is set to auto-increment, and the USER_ID column is a foreign key referencing the ID column
         * of the "user" table.
         *
         * @param sqLiteDatabase The database to create the table in.
         */
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL("" +
                        "CREATE TABLE m_order(" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "USER_ID INTEGER NOT NULL," +
                        "DELIVERY INTEGER NOT NULL DEFAULT 0," +
                        "STATUS INTREGER NOT NULL DEFAULT 0," +
                        "CREATED TEXT NOT NULL," +
                        "FOREIGN KEY (USER_ID) REFERENCES user(ID))");
            } catch (SQLException e){

            }
        }

        /**
         * Retrieves all orders from the database and returns them as an ArrayList.
         *
         * @return An ArrayList of all orders in the database.
         */
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

        /**
         * Updates an order in the database with the given updated order object.
         *
         * @param updated The updated order object to be saved in the database.
         */
        public void updateOrder(Order updated){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put("USER_ID", updated.getUserID());
            cv.put("DELIVERY", updated.getDelivery().value);
            cv.put("STATUS", updated.getStatus().status);
            cv.put("CREATED", User.formatter.format(updated.getCreated()));

            db.update("m_order", cv, "ID = ?", new String[]{String.valueOf(updated.id)});
            db.close();
        }

        /**
         * Returns the order with the specified ID.
         *
         * @param orderID The ID of the order to retrieve.
         * @return The order with the specified ID, or null if no such order exists.
         */
        public Order getSpecificOrder(int orderID){
            for(Order o: getAllOrders()){
                if(o.id == orderID){
                    return o;
                }
            }
            return null;
        }

        /**
         * Retrieves all orders for a given user ID from the database.
         *
         * @param userID The ID of the user whose orders are being retrieved.
         * @return An ArrayList of Order objects representing the user's orders.
         */
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

        /**
         * Adds an order to the database.
         *
         * @param c The context of the application.
         * @param o The order to be added.
         * @return True if the order was successfully added, false otherwise.
         */
        public boolean addOrder(Context c, Order o){
            SQLiteDatabase data = this.getWritableDatabase();
            ContentValues output = new ContentValues();

            output.put("USER_ID", o.userID);
            output.put("DELIVERY", o.delivery.value);
            output.put("STATUS", o.status.status);
            output.put("CREATED", User.formatter.format(LocalDateTime.now()));

            if(o.orderProducts != null){
                for(OrderProduct op: o.orderProducts){
                    OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c);
                    opHelper.addOrderProduct(op);
                }
            }

            data.insert("m_order", null, output);
            data.close();
            return true;
        }

        /**
         * Adds an order to the database based on the items in the basket, the user who placed the order, and the delivery method.
         *
         * @param c The context of the application.
         * @param basket A HashMap containing the IDs of the products in the basket and their corresponding quantities.
         * @param forUser The user who placed the order.
         * @param d The delivery method chosen by the user.
         */
        public void addOrderFromBasket(Context c, HashMap<Integer, Integer> basket, User forUser, DeliveryMethod d){
            //Initialise new order object
            Order output = new Order();
            //Get next available Order ID - SQLite starts AUTOINCREMENT from 1, hence the +1
            int orderID = getAllOrders().size()+1;
            //Initialise DBHelper instances for Products and OrderProducts
            Product.DBHelper prodHelper = new Product.DBHelper(c);
            OrderProduct.DBHelper opHelper = new OrderProduct.DBHelper(c);

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

        /**
         * Removes an order from the database and updates the order's status to cancelled.
         * This method will also return all products found in the order back to the products' stock level.
         * 
         * @param toRemove The order to remove from the database.
         * @param c The context of the application.
         */
        public void removeOrder(Order toRemove, Context c){
            SQLiteDatabase db = this.getWritableDatabase();
            toRemove.updateOrder(c, true);
            db.delete("m_order", "ID = ?", new String[]{String.valueOf(toRemove.getId())});
            db.close();
        }

        /**
         * Called when the database needs to be upgraded. This method drops the existing table
         * and creates a new one.
         *
         * @param sqLiteDatabase The database to be upgraded
         * @param i The old version of the database
         * @param i1 The new version of the database
         */
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS m_order");
            onCreate(sqLiteDatabase);
        }

        /**
         * Called when the database is opened. This method is called after the database connection has been
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
