package com.neolabs.api;


import com.neolabs.response.PlayerInfoResponse;
import com.neolabs.response.RatingResponse;
import com.neolabs.utils.Constants;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface WebInterface {

    @GET(Constants.API_PLAYER_INFO)
    void getPlayerInfo(@Query("player_id") String playerId, WebCallback<PlayerInfoResponse> callback);

    @GET(Constants.API_RATING)
    void getRating(WebCallback<RatingResponse> callback);

    @FormUrlEncoded
    @POST(Constants.API_BUY_SKIN)
    void buySkin(@Field("player_id") String playerId, @Field("skin_id") String skinId, WebCallback<ServerResponse> callback);

}