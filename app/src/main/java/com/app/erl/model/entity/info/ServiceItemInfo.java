
package com.app.erl.model.entity.info;

import org.parceler.Parcel;

@Parcel
public class ServiceItemInfo {
    int id, qty;
    String name, price;

    public ServiceItemInfo() {

    }

    public ServiceItemInfo(int id, String name, String price, int quantity) {
        this.id = id;
        this.qty = quantity;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return qty;
    }

    public void setQuantity(int quantity) {
        this.qty = quantity;
    }
}



