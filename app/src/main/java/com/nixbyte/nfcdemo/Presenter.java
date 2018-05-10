package com.nixbyte.nfcdemo;

public interface Presenter<V> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
