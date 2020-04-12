package com.android.coronahack.habitus_venditor.helpers;

public class EnterMeds {

    private String name, quantity;

    public EnterMeds () {

    }

    public EnterMeds(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
