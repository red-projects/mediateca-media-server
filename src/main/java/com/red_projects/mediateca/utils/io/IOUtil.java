package com.red_projects.mediateca.utils.io;

public class IOUtil {

    public static String covertToValidName(String name) {
        return name.replace(" ", "_");
    }
}
