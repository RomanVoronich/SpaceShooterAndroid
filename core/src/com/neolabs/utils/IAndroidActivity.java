package com.neolabs.utils;

public interface IAndroidActivity {

    void purchase(String sku, IPurchaseFinished cb);

    void comsume(String sku, IConsumeFinished cb);

    String getAndroidId();

    void loginSocial(int socialId);
}
