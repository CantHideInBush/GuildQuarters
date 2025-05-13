package com.canthideinbush.guildquarters.http.templating;

import com.canthideinbush.guildquarters.http.GQHttpServer;
import com.sun.net.httpserver.HttpExchange;

public class LocalAddressPlaceholder implements HTMLPlaceholder {
    @Override
    public String htmlPrefix() {
        return "localAddress";
    }

    @Override
    public String parse(HttpExchange exchange, String data) {
        return "https://" + GQHttpServer.instance.address.getHostName() + ":" + GQHttpServer.instance.address.getPort() + "/" + data;
    }
}
