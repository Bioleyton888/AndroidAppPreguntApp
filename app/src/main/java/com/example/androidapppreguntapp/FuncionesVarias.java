package com.example.androidapppreguntapp;

import android.net.wifi.WifiManager;

public class FuncionesVarias {
    public String ipv4(){
        return "192.168.1.94";
    }

    public String port(){
        return "80";
    }

    public static boolean convertToBoolean(String value) {
        boolean returnValue = false;
        if ("1".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) ||
                "true".equalsIgnoreCase(value) || "on".equalsIgnoreCase(value))
            returnValue = true;
        return returnValue;
    }

}
