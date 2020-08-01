
package com.app.erl.model.entity.response;


import com.app.erl.model.entity.info.TransactionHistoryInfo;

import java.util.List;

public class TransactionHistoryResponse extends BaseResponse {
    private List<TransactionHistoryInfo> info;

    public List<TransactionHistoryInfo> getInfo() {
        return info;
    }

    public void setInfo(List<TransactionHistoryInfo> info) {
        this.info = info;
    }
}

