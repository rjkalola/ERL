package com.app.erl.callback;

/**
 * @AutherBy Dhaval Jivani
 */
public interface BaseListener<T> {
    void onSuccess(T userResponse);
    void onError(String exception, String errorCode);
}
