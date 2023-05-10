package com.github.sebastiankg02.csy2061as2.data;

import java.util.HashMap;

public class Basket {
    private static HashMap<Integer, Integer> contents;
    private static boolean hasInit;

    public static void init(){
        if(!hasInit) {
            contents = new HashMap<Integer, Integer>();
            hasInit = true;
        }
    }

    public static void addToBasket(Product p){
        addToBasket(p, 1);
    }

    public static void addToBasket(Product p, int quantity){
        if(!contents.containsKey(p.getId())) {
            contents.put(p.getId(), quantity);
        } else {
            contents.replace(p.getId(), contents.get(p.getId())+quantity);
        }
    }

    public static void removeFromBasket(Product p){
        if(contents.containsKey(p.getId())) {
            contents.remove(p.getId());
        }
    }

    public static void removeFromBasket(Product p, int quantity){
        if(contents.containsKey(p.getId())) {
            if (quantity <= 0) {
                contents.remove(p.getId());
            } else {
                contents.replace(p.getId(), contents.get(p.getId())-quantity);
            }
        }
    }

    public static HashMap<Integer, Integer> getContents() {
        return contents;
    }

    public static void setContents(HashMap<Integer, Integer> contents) {
        Basket.contents = contents;
    }

    public static void emptyBasket(){
        contents.clear();
    }
}
