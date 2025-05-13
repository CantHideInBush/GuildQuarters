package com.canthideinbush.guildquarters.http;

import com.canthideinbush.guildquarters.http.templating.PlaceholderHandler;
import com.canthideinbush.guildquarters.http.templating.SupplierPlaceholder;
import com.canthideinbush.guildquarters.utils.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class FileHttpHandler implements HttpHandler {


    private final HttpServer server;

    public FileHttpHandler(HttpServer server) {
        this.server = server;
        placeholders.registerPlaceholder(new SupplierPlaceholder(suppliers));
    }

    private final ArrayList<String> files = new ArrayList<>();


    /**
     *
     * @param file path to file
     * correct: css/style.css
     * incorrect: /css/style.css
     */
    public void addFile(String file) {
        file = file.replace("\\", "/");
        if (files.contains(file)) return;
        server.createContext("/" + file, this);
        files.add(file);
    }

    public void removeFile(String file) {
        file = file.replace("\\", "/");
        if (files.contains(file)) {
            files.remove(file);
            server.removeContext(file);
        }
    }

    private final PlaceholderHandler placeholders = new PlaceholderHandler();


    private static final Map<String, Supplier<String>> suppliers = new HashMap<>();

    public static void registerSupplier(String name, Supplier<String> supplier) {
        suppliers.put(name, supplier);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        OutputStream stream = exchange.getResponseBody();
        String requested = exchange.getRequestURI().toString()
                .substring(1).split("\\?")[0];




        if (!files.contains(requested)) {
            exchange.sendResponseHeaders(404, 0);
            exchange.close();
            return;
        }


        InputStream fileStream = Utils.getFileInputStream("https" + File.separator + requested);

        byte[] bytes;

        String format = Utils.getFileFormat(requested);
        if ("css".equals(format) || "html".equals(format)) {
            String response = placeholders.parse(exchange, new String(fileStream.readAllBytes()));
            bytes = response.getBytes();
        }
        else {
            bytes = fileStream.readAllBytes();
        }








        exchange.sendResponseHeaders(200, bytes.length);

        stream.write(bytes);

        stream.flush();

        stream.close();
    }
}
