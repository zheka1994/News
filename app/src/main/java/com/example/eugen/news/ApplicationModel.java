package com.example.eugen.news;

import java.util.ArrayList;


public class ApplicationModel {
    private static ApplicationModel applicationModel = null;
    private ArrayList<Item> items;
    public static ApplicationModel getInstance(){
        if(applicationModel == null){
            applicationModel = new ApplicationModel();
        }
        return applicationModel;
    }
    private ApplicationModel(){
        items = new ArrayList<>();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    public Item getItemById(String guid){
        for(Item item: items){
            String str = item.getGuid();
            if(str.equals(guid)){
                return item;
            }
        }
        return null;
    }
}
