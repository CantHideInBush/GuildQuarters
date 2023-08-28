package com.canthideinbush.guildquarters.utils;

import com.canthideinbush.guildquarters.GuildQ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Utils {

    public static boolean isNumeric(String string) {

        for (int i = 0; i < string.length(); i++) {
            if (i == 0 && string.charAt(0) == '-') {
                if (string.length() == 1) return false;
                else continue;
            }
            if (Character.digit(string.charAt(i), 10) < 0) return false;
        }


        return true;
    }

    public static boolean testing = false;

    public static InputStream getResourceAsStream(String resource) {
        if (testing) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
        }
        else return GuildQ.getInstance().getResource(resource);
    }

    public static URL getResource(String resource) {
        if (testing) {
            return Thread.currentThread().getContextClassLoader().getResource(resource);
        }
        else {
            try {
                return new URL("file:///" + GuildQ.getInstance().getDataFolder().getAbsolutePath().replace(" ", "%20") + "\\" + resource.replace("/", "\\"));
            } catch (
                    MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static InputStream getFileInputStream(String file) {
        if (testing) {
            return getResourceAsStream(file);
        }
        else {
            try {
                return new FileInputStream(new File(GuildQ.getInstance().getDataFolder(), file));
            } catch (
                    FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String getFileFormat(String file) {
        if (!file.contains(".")) return "";
        return file.split("\\.")[1];
    }

}
