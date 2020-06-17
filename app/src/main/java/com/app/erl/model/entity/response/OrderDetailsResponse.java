
package com.app.erl.model.entity.response;


import com.app.erl.model.entity.info.OrderItemInfo;

import java.util.List;

public class OrderDetailsResponse extends BaseResponse {
    private List<OrderItemInfo> info;
    private int total_price, amount_pay;
    private String order_no, wallet;

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

    public int getAmount_pay() {
        return amount_pay;
    }

    public void setAmount_pay(int amount_pay) {
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
}

