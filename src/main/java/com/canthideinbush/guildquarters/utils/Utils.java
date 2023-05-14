package com.canthideinbush.guildquarters.utils;

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


}
