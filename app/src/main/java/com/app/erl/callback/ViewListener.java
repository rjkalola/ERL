package com.app.erl.callback;

import com.app.erl.network.RetrofitException;

public interface ViewListener {

    void showProgress();

    void hideProgress();

    void showApiError(RetrofitException retrofitException, String errorCode);
}
