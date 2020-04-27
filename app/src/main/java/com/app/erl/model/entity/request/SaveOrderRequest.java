package com.app.erl.model.entity.request;

import com.app.erl.model.entity.info.ServiceItemInfo;

import java.util.ArrayList;
import java.util.List;

public class SaveOrderRequest {
    private int pickup_hour_id, address_id;
    private String pickup_date;
    private List<ServiceItemInfo> order = new ArrayList<>();

    public int getPickup_hour_id() {
        return pickup_hour_id;
    }

    public void setPickup_hour_id(int pickup_hour_id) {
        this.pickup_hour_id = pickup_hour_id;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public List<ServiceItemInfo> getOrder() {
        return order;
    }

    public void setOrder(List<ServiceItemInfo> order) {
        this.order = order;
    }
}
