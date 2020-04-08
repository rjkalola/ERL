package com.app.erl.callback;

public interface Presenter<T> {

    void createView(T view);

    void destroyView();
}
