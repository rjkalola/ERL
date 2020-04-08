
package com.app.erl.model.entity.response;


import org.parceler.Parcel;

@Parcel
public class UserResponse extends BaseResponse {
    User info;
    int user_status;
    int is_valid;

    public User getInfo() {
        return info;
    }

    public void setInfo(User info) {
        this.info = info;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }
}

