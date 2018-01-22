package com.joostit.vfradar.utilities;

/**
 * Created by Joost on 22-1-2018.
 */

public final class StringValue {

    public static Boolean nullOrEmpty(String input) {
        return ((input == null) || (input.trim().equals("")));
    }

}
