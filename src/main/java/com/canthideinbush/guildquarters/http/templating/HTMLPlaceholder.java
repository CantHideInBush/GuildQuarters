package com.canthideinbush.guildquarters.http.templating;

import com.sun.net.httpserver.HttpExchange;

public interface HTMLPlaceholder {


    String htmlPrefix();
    String parse(HttpExchange exchange, String data);

}
