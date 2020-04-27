
package com.app.erl.model.entity.response;

import com.app.erl.model.entity.info.PickUpTimeInfo;
import com.app.erl.model.entity.request.SaveAddressRequest;

import java.util.List;

public class OrderResourcesResponse extends BaseResponse {
    private List<PickUpTimeInfo> pickup_hours;
    private SaveAddressRequest info;

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
}



