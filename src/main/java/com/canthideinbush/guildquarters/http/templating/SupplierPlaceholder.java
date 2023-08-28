package com.canthideinbush.guildquarters.http.templating;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;
import java.util.function.Supplier;

public class SupplierPlaceholder implements HTMLPlaceholder{

    public SupplierPlaceholder(Map<String, Supplier<String>> data) {
        this.data = data;
    }

    private final Map<String, Supplier<String>> data;

    @Override
    public String htmlPrefix() {
        return "supply";
    }

    @Override
    public String parse(HttpExchange exchange, String data) {
        return this.data.containsKey(data) ? this.data.get(data).get() : "";
    }
}
