package com.neolabs.api;

import com.neolabs.model.PlayerT;
import com.neolabs.utils.Constants;
import com.google.gson.Gson;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketApi {
    public Socket socket;

    // region singleton
    public static final SocketApi instance = new SocketApi();

    private SocketApi() {
        try {
            //initConnection();
        } catch (Throwable ignored) {
            System.out.println("[ERR] SocketApi: " + ignored);
        }
    }

    public void initConnection(PlayerT player) {
        try {
            Gson gson = new Gson();
            IO.Options opts = new IO.Options();
            opts.reconnection = true;
            opts.reconnectionDelay = 1000;
            opts.timeout = 10000;
            opts.port = Constants.PORT_IO;
            opts.query = "player=" + gson.toJson(player);
            socket = IO.socket(Constants.END_POINT_IO, opts);
        } catch (URISyntaxException e) {
            System.out.println("[ERR] connect:" + e);
        }
    }

    public static SocketApi getInstance() {
        return instance;
    }
    // endregion

    public void connect() {
        socket.connect();
    }

    public Socket getSocket() {
        return socket;
    }
}
