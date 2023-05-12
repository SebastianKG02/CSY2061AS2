package com.github.sebastiankg02.csy2061as2.data;

import java.util.HashMap;

/**
 * A Basket class that contains a HashMap of contents and provides methods to initialize, set, and empty the basket.
 */
public class Basket {
    private static HashMap<Integer, Integer> contents;
    private static boolean hasInit;

    /**
     * Initializes the contents of the HashMap if it has not already been initialized.
     * This method should be called before any operations are performed on the HashMap.
     */
    public static void init(){
        if(!hasInit) {
            contents = new HashMap<Integer, Integer>();
            hasInit = true;
        }
    }

    /**
     * Adds a single product to the basket with a quantity of 1.
     *
     * @param p The product to add to the basket.
     */
    public static void addToBasket(Product p){
        addToBasket(p, 1);
    }

    /**
     * Adds a product and its quantity to the basket.
     *
     * @param p The product to add to the basket.
     * @param quantity The quantity of the product to add to the basket.
     */
    public static void addToBasket(Product p, int quantity){
        if(!contents.containsKey(p.getId())) {
            contents.put(p.getId(), quantity);
        } else {
            contents.replace(p.getId(), contents.get(p.getId())+quantity);
        }
    }

    /**
     * Removes a product from the basket if it exists.
     *
     * @param p The product to remove from the basket.
     */
    public static void removeFromBasket(Product p){
        if(contents.containsKey(p.getId())) {
            contents.remove(p.getId());
        }
    }

    /**
     * Removes a specified quantity of a product from the basket.
     *
     * @param p The product to remove from the basket
     * @param quantity The quantity of the product to remove
     */
    public static void removeFromBasket(Product p, int quantity){
        if(contents.containsKey(p.getId())) {
            if (quantity <= 0) {
                contents.remove(p.getId());
            } else {
                contents.replace(p.getId(), contents.get(p.getId())-quantity);
            }
        }
    }

    /**
     * Returns the contents of this HashMap.
     *
     * @return The contents of the HashMap.
     */
    public static HashMap<Integer, Integer> getContents() {
        return contents;
    }

    /**
     * Sets the contents of the basket to the given HashMap.
     *
     * @param contents The HashMap containing the contents to set.
     */
    public static void setContents(HashMap<Integer, Integer> contents) {
        Basket.contents = contents;
    }

    /**
     * Clears the contents of the basket.
     */
    public static void emptyBasket(){
        contents.clear();
    }
}
