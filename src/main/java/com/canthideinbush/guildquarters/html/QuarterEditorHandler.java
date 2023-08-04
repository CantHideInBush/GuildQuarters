package com.canthideinbush.guildquarters.html;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class QuarterEditorHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        }
        else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostRequest(exchange);
        }

    }

    private void handlePostRequest(HttpExchange exchange) {

    }

    private void handleGetRequest(HttpExchange exchange) {
    }
}
