package com.example.user.project1;

/**
 * Created by User on 11/3/2016.
 */
public class WiFi {
    String SSID;
    int level;
    public double distance;
    public double x;
    public double y;
    public double z;
    WiFi() {
    }

    public WiFi(String SSID, int level) {
        this.SSID = SSID;
        this.level = level;;
    }

    public WiFi(String SSID, double x, double y, double z) {
        this.SSID = SSID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "Wifi{" +
                "SSID='" + SSID + '\'' +
                ", level=" + level +
                ", distance=" + distance +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public String getSSID() {
        return SSID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }
}
