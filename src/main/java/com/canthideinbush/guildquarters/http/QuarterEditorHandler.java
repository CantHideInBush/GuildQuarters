package com.canthideinbush.guildquarters.http;

import com.canthideinbush.guildquarters.http.templating.LocalAddressPlaceholder;
import com.canthideinbush.guildquarters.http.templating.PlaceholderHandler;
import com.canthideinbush.guildquarters.http.templating.SupplierPlaceholder;
import com.canthideinbush.guildquarters.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class QuarterEditorHandler implements HttpHandler {


    private final GQHttpServer server;

    private final PlaceholderHandler placeholders = new PlaceholderHandler();


    private static final Map<String, Supplier<String>> suppliers = new HashMap<>();

    public static void registerSupplier(String name, Supplier<String> supplier) {
        suppliers.put(name, supplier);
    }

    public QuarterEditorHandler(GQHttpServer server) {
        this.server = server;
        placeholders.registerPlaceholder(new LocalAddressPlaceholder());
        placeholders.registerPlaceholder(new SupplierPlaceholder(suppliers));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);

        }
        else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostRequest(exchange);
        }

    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        server.logger.info(exchange.getRequestURI().toString());

        OutputStream stream = exchange.getResponseBody();


        StringBuilder htmlBuilder = new StringBuilder();


        InputStream responseFile = Utils.getFileInputStream("https/htdocs/index.html");
        assert responseFile != null;
        htmlBuilder.append(new String(responseFile.readAllBytes()));

        String htmlResponse = placeholders.parse(exchange, htmlBuilder.toString());

        exchange.sendResponseHeaders(200, htmlResponse.length());

        stream.write(htmlResponse.getBytes());

        stream.flush();

        stream.close();
        exchange.close();
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {

        OutputStream stream = exchange.getResponseBody();


        StringBuilder htmlBuilder = new StringBuilder();


        InputStream responseFile = Utils.getResourceAsStream("https/htdocs/index.html");
        assert responseFile != null;
        htmlBuilder.append(new String(responseFile.readAllBytes()));

        String htmlResponse = placeholders.parse(exchange, htmlBuilder.toString());

        exchange.sendResponseHeaders(200, htmlResponse.length());

        stream.write(htmlResponse.getBytes());

        stream.flush();

        stream.close();
    }

    public GQHttpServer getServer() {
        return server;
    }



}
