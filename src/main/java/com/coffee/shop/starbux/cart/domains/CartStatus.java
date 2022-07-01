package com.coffee.shop.starbux.cart.domains;

public enum CartStatus {

    OPEN("OPEN"),
    PROGRESS("PROGRESS"),
    CANCELED("CANCELED"),
    FINISH("FINISH");

    private String description;

    private CartStatus(String desc){
        this.description = desc;
    }

    public String getDescription() {
        return this.description;
    }
}
