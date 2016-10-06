package com.neolabs.api;

import com.neolabs.model.PlayerInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class ServerResponse {

    private int status = 0;

    public boolean isSuccess() {
        return status == 1;
    }

/*
    public ServerResponse(){
        System.out.println("ServerResponse status" + status );
        System.out.println("ServerResponse message" + message );
        System.out.println("ServerResponse data" + data );
    }*/

}
