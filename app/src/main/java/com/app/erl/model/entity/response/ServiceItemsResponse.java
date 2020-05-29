
package com.app.erl.model.entity.response;

import com.app.erl.model.entity.info.ServiceInfo;
import com.app.erl.model.entity.info.ServiceItemInfo;

import java.util.List;

public class ServiceItemsResponse extends BaseResponse {
    private List<ServiceInfo> info;

    public List<ServiceInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ServiceInfo> info) {
        this.info = info;
    }
}



