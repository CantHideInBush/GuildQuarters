package com.canthideinbush.guildquarters.http.templating;

import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderHandler {

    public PlaceholderHandler() {}

    private final ArrayList<HTMLPlaceholder> placeholders = new ArrayList<>();

    public void registerPlaceholder(HTMLPlaceholder placeholder) {
        placeholders.add(placeholder);
    }


    public String parse(HttpExchange exchange, String string) {

        Matcher matcher = regexPattern.matcher(string);

        while (matcher.find()) {
            Optional<HTMLPlaceholder> placeholder = placeholders.stream().filter((hP) -> hP.htmlPrefix().equals(matcher.group(1))).findAny();
            if (placeholder.isPresent()) string = string.replaceFirst(Pattern.quote(matcher.group(0)), placeholder.get().parse(exchange, matcher.group(2)));
        }
        return string;
    }

    /**
     * Regex capturing template tags
     * Example: <p>obj:{player}</p>
     * <p>2st group: obj</p>
     * 3rd group: player
     */
    private final Pattern regexPattern = Pattern.compile(" ((?:.(?! ))+):\\{(.*?)}", Pattern.MULTILINE); ;



}
