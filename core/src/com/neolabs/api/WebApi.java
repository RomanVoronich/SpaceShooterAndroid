package com.neolabs.api;

import com.neolabs.response.PlayerInfoResponse;
import com.neolabs.response.RatingResponse;
import com.neolabs.utils.Constants;

import retrofit.RestAdapter;

//  String id = activity.getAndroidId();
public class WebApi {

    // region singleton
    public static final WebApi instance = new WebApi();

    private WebApi() {
        initConnection();
    }

    public static WebApi getInstance() {
        return instance;
    }

    private void initConnection() {
        try {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.END_POINT_WEB)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            service = restAdapter.create(WebInterface.class);
        } catch (Throwable ignored) {
        }
    }


    // endregion

    //region поля и акцессоры
    private WebInterface service;
    //endregion

    public void getPlayerInfo(String playerId, WebCallback<PlayerInfoResponse> cb) {
        service.getPlayerInfo(playerId, cb);
    }


    public void getRating(WebCallback<RatingResponse> cb) {
        service.getRating(cb);
    }

    public void buySkin(String playerId, String skinId, WebCallback<ServerResponse> cb) {
        service.buySkin(playerId, skinId, cb);
    }

}
