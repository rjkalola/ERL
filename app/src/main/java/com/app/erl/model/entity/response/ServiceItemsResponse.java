
package com.app.erl.model.entity.response;

import com.app.erl.model.entity.info.ServiceItemInfo;

import java.util.List;

public class ServiceItemsResponse extends BaseResponse {
    private List<ServiceItemInfo> info;
    private String name;

    public List<ServiceItemInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ServiceItemInfo> info) {
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



