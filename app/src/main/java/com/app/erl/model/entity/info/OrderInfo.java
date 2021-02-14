
package com.app.erl.model.entity.info;

import com.app.erl.model.entity.request.SaveAddressRequest;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class OrderInfo {
    int id, status_id;
    String order_no, pickup_date, pickup_time, delivery_note, total_price, ordered_at, status,payment_type,order_type,amount_pay,address,city_name;
    List<ServiceItemInfo> order;
    boolean show_payment_button,show_feedback;
    SaveAddressRequest info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
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

    public String getDelivery_note() {
        return delivery_note;
    }

    public void setDelivery_note(String delivery_note) {
        this.delivery_note = delivery_note;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getOrdered_at() {
        return ordered_at;
    }

    public void setOrdered_at(String ordered_at) {
        this.ordered_at = ordered_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public List<ServiceItemInfo> getOrder() {
        return order;
    }

    public void setOrder(List<ServiceItemInfo> order) {
        this.order = order;
    }

    public SaveAddressRequest getInfo() {
        return info;
    }

    public void setInfo(SaveAddressRequest info) {
        this.info = info;
    }

    public String getAmount_pay() {
        return amount_pay;
    }

    public void setAmount_pay(String amount_pay) {
        this.amount_pay = amount_pay;
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

    public boolean isShow_feedback() {
        return show_feedback;
    }

    public void setShow_feedback(boolean show_feedback) {
        this.show_feedback = show_feedback;
    }
}



