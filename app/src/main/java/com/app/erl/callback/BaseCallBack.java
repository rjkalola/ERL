package com.app.erl.callback;

import android.content.Context;

public interface BaseCallBack {

    void showProgress(Context context, String message);

    void hideProgress();
}