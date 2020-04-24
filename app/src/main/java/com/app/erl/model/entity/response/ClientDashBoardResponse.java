
package com.app.erl.model.entity.response;

import com.app.erl.model.entity.info.ClientDashBoardInfo;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ClientDashBoardResponse extends BaseResponse {
    List<ClientDashBoardInfo> info;

    public void copyData(ClientDashBoardResponse data) {
        this.info = data.getInfo();
    }

    public List<ClientDashBoardInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ClientDashBoardInfo> info) {
        this.info = info;
    }
}



