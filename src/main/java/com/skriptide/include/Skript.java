package com.skriptide.include;

/**
 * Created by yannh on 27.01.2017.
 */
public class Skript {

    private String name;
    private String version;

    public Skript(String name, String version, String path) {
        this.name = name;
        this.version = version;
        this.path = path;
    }


    private String path;


    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getPath() {
        return path;
    }

}
