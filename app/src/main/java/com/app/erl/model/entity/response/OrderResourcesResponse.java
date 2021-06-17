
package com.app.erl.model.entity.response;

import com.app.erl.model.entity.info.PickUpTimeInfo;
import com.app.erl.model.entity.request.SaveAddressRequest;

import java.util.List;

public class OrderResourcesResponse extends BaseResponse {
    private List<PickUpTimeInfo> pickup_hours;
    private SaveAddressRequest info;
    private int max_wallet_deduction;
    private double wallet;

    public List<PickUpTimeInfo> getPickup_hours() {
        return pickup_hours;
    }

    public void setPickup_hours(List<PickUpTimeInfo> pickup_hours) {
        this.pickup_hours = pickup_hours;
    }

    public SaveAddressRequest getInfo() {
        return info;
    }

    public void setInfo(SaveAddressRequest info) {
        this.info = info;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public int getMax_wallet_deduction() {
        return max_wallet_deduction;
    }

    public void setMax_wallet_deduction(int max_wallet_deduction) {
        this.max_wallet_deduction = max_wallet_deduction;
    }
}



