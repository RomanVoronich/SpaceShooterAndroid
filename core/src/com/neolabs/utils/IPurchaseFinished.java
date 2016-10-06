package com.neolabs.utils;

public interface IPurchaseFinished {
    void onIabPurchaseFinished(boolean result, String sku);
}
