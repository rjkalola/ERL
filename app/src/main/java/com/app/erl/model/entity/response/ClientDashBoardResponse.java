
package com.app.erl.model.entity.response;

import com.app.erl.model.entity.info.ClientDashBoardInfo;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ClientDashBoardResponse extends BaseResponse {
    List<ClientDashBoardInfo> info;
    String contact_number,contact_email;
    int wallet;

    public void copyData(ClientDashBoardResponse data) {
        this.info = data.getInfo();
    }

    public List<ClientDashBoardInfo> getInfo() {
        return info;
    }

    public void setInfo(List<ClientDashBoardInfo> info) {
        this.info = info;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}



