package com.kit.projectdesign.util;

public class MyTokenStore {
    private static String token;

    public static void set(String t){ token = t; }
    public static String get(){ return token; }
}

