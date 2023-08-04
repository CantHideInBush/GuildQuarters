package com.canthideinbush.guildquarters.html;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class GQHttpServer {

    private final HttpServer server;

    public GQHttpServer() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 15535), 0);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize() {
        server.createContext("/editor", new QuarterEditorHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
    }




}
