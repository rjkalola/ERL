
package com.app.erl.model.entity.response;


import com.app.erl.model.entity.info.OrderItemInfo;

import java.util.List;

public class OrderDetailsResponse extends BaseResponse {
    private List<OrderItemInfo> info;
    private int total_price;
    private String order_no, wallet,amount_pay,address,city_name,service_type_name, pickup_date, pickup_time, deliver_date,delivery_time;
    private boolean show_payment_button;

    public List<OrderItemInfo> getInfo() {
        return info;
    }

    public void setInfo(List<OrderItemInfo> info) {
        this.info = info;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getAmount_pay() {
        return amount_pay;
    }

    public void setAmount_pay(String amount_pay) {
        this.amount_pay = amount_pay;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public boolean isShow_payment_button() {
        return show_payment_button;
    }

    public void setShow_payment_button(boolean show_payment_button) {
        this.show_payment_button = show_payment_button;
    }

    public String getService_type_name() {
        return service_type_name;
    }

    public void setService_type_name(String service_type_name) {
        this.service_type_name = service_type_name;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getDeliver_date() {
        return deliver_date;
    }

    public void setDeliver_date(String deliver_date) {
        this.deliver_date = deliver_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }
}

